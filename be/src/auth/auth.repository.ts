import { DataSource, IsNull, Repository, Not, In } from "typeorm";
import {
    ConflictException,
    Injectable,
    BadRequestException,
} from "@nestjs/common";
import { AuthRefreshTokenEntity } from "./entity/auth.refreshtoken.entity";

@Injectable()
export class AuthRepository extends Repository<AuthRefreshTokenEntity> {
    constructor(private dataSource: DataSource) {
        super(AuthRefreshTokenEntity, dataSource.createEntityManager());
    }
}