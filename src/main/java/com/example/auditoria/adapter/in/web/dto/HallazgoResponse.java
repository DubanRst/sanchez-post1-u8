package com.example.auditoria.adapter.in.web.dto;

import com.example.auditoria.domain.entity.HallazgoAuditoria;
import com.example.auditoria.domain.valueobject.EstadoHallazgo;
import com.example.auditoria.domain.valueobject.PlanRemediacion;
import com.example.auditoria.domain.valueobject.Severidad;

import java.time.LocalDate;

public record HallazgoResponse(String id, String titulo, String descripcion, String areaResponsable,
                               Severidad severidad, EstadoHallazgo estado, LocalDate fechaDeteccion,
                               LocalDate fechaCierre, PlanRemediacion planRemediacion) {

    public static HallazgoResponse desde(HallazgoAuditoria h) {
        return new HallazgoResponse(h.getId().toString(), h.getTitulo(), h.getDescripcion(),
                h.getAreaResponsable(), h.getSeveridad(), h.getEstado(), h.getFechaDeteccion(),
                h.getFechaCierre(), h.getPlanRemediacion());
    }
}