import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  CreateDateColumn,
  UpdateDateColumn,
  DeleteDateColumn,
} from "typeorm";

@Entity()
export class User {
  @PrimaryGeneratedColumn("increment")
  id: number;

  @Column({ type: "varchar", length: 20, unique: true })
  nickName: string;

  @Column({ type: "varchar", length: 50, unique: true })
  email: string;

  @Column({ type: "varchar", length: 11 })
  birthdate: string;

  @Column({ type: "varchar", length: 20 })
  region: string;

  @Column({ type: "boolean" })
  isMale: boolean;

  @Column({ type: "varchar", length: 50, nullable: true })
  password: string | null;

  @Column({ type: "varchar", length: 20, nullable: true })
  social_provider: string | null;

  @CreateDateColumn({ type: "timestamp" })
  created_at: Date;

  @DeleteDateColumn({ type: "timestamp", nullable: true })
  deleted_at: Date | null;

  @UpdateDateColumn({ type: "timestamp" })
  updated_at: Date;
}
