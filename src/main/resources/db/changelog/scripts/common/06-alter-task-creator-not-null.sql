--liquibase formatted sql
--changeset mrynski:alterTaskCreatorNotNull

ALTER TABLE tasks MODIFY creator_id BIGINT NOT NULL;