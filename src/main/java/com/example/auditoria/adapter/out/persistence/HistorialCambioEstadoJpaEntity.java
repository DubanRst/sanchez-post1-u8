package com.example.auditoria.adapter.out.persistence;

import com.example.auditoria.domain.valueobject.EstadoHallazgo;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_cambios_estado")
public class HistorialCambioEstadoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String hallazgoId;
    @Enumerated(EnumType.STRING)
    private EstadoHallazgo estadoAnterior;
    @Enumerated(EnumType.STRING)
    private EstadoHallazgo estadoNuevo;
    private String motivo;
    private LocalDateTime fecha;

    protected HistorialCambioEstadoJpaEntity() {}

    public HistorialCambioEstadoJpaEntity(String hallazgoId, EstadoHallazgo estadoAnterior,
                                          EstadoHallazgo estadoNuevo, String motivo, LocalDateTime fecha) {
        this.hallazgoId = hallazgoId;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.motivo = motivo;
        this.fecha = fecha;
    }

    public Long getId() { return id; }
    public String getHallazgoId() { return hallazgoId; }
    public EstadoHallazgo getEstadoAnterior() { return estadoAnterior; }
    public EstadoHallazgo getEstadoNuevo() { return estadoNuevo; }
    public String getMotivo() { return motivo; }
    public LocalDateTime getFecha() { return fecha; }
}