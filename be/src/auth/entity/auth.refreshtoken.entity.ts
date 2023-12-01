import { Entity, Column, PrimaryColumn } from 'typeorm';

@Entity("AuthRefreshToken")
export class AuthRefreshTokenEntity {
    @PrimaryColumn()
    id: number;

    @Column({ type: 'varchar', length: 300 })
    refreshToken: string
}
