/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author nam
 */
public class Export_Batches {
    private UUID batch_id;
    private LocalDateTime  createAT;
    private LocalDateTime  date_of_import;
    private int status;

    public Export_Batches() {
    }

    public Export_Batches(UUID batch_id, LocalDateTime createAT, LocalDateTime date_of_import, int status) {
        this.batch_id = batch_id;
        this.createAT = createAT;
        this.date_of_import = date_of_import;
        this.status = status;
    }

    public UUID getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(UUID batch_id) {
        this.batch_id = batch_id;
    }

    public LocalDateTime getCreateAT() {
        return createAT;
    }

    public void setCreateAT(LocalDateTime createAT) {
        this.createAT = createAT;
    }

    public LocalDateTime getDate_of_import() {
        return date_of_import;
    }

    public void setDate_of_import(LocalDateTime date_of_import) {
        this.date_of_import = date_of_import;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Export_batches{" + "batch_id=" + batch_id + ", createAT=" + createAT + ", date_of_import=" + date_of_import + ", status=" + status + '}';
    }
}
