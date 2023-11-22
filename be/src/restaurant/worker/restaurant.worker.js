const { parentPort, workerData } = require('worker_threads');
const axios = require('axios');
const proj4 = require('proj4');

const key = 'api키 적으세요';

const data = workerData;

async function workerTask() {
    const tm2097 = "+proj=tmerc +lat_0=38 +lon_0=127.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43";
    const wgs84 = "EPSG:4326";

    const pageElementNum = data.pageElementNum;
    const apiUrl = `http://openapi.seoul.go.kr:8088/${key}/json/LOCALDATA_072404/${pageElementNum}/${pageElementNum + 999}/`;

    const response = await axios.get(apiUrl)

    const result = { data: [], lastPage: false };
    console.log(response.data);
    if (response.data.RESULT && response.data.RESULT.CODE === "INFO-200") {
        result.lastPage = true;
    } else {
        response.data.LOCALDATA_072404.row.forEach(element => {
            const tmX = parseFloat(element.X);
            const tmY = parseFloat(element.Y);
            if (!isNaN(tmX) && !isNaN(tmY) && element.DTLSTATENM === "영업") {
                const [lon, lat] = proj4(tm2097, wgs84, [tmX, tmY]);
                result.data.push({
                    name: element.BPLCNM,
                    location: { type: 'Point', coordinates: [lon, lat] },
                    address: element.SITEWHLADDR,
                    category: element.UPTAENM,
                    phoneNumber: element.SITETEL,
                });
            }
        });
    }
    parentPort.postMessage(result);
}


workerTask();
