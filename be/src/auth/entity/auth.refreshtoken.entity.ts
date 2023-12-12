import { Entity, Column, PrimaryColumn } from 'typeorm';

@Entity("auth_token")
export class AuthRefreshTokenEntity {
    @PrimaryColumn()
    id: number;

    @Column({ type: 'varchar', length: 300 })
    accessToken: string

    @Column({ type: 'varchar', length: 300 })
    refreshToken: string
}
