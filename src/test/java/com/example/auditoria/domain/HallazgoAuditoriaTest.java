package com.example.auditoria.domain;

import com.example.auditoria.domain.entity.HallazgoAuditoria;
import com.example.auditoria.domain.valueobject.EstadoHallazgo;
import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.domain.valueobject.PlanRemediacion;
import com.example.auditoria.domain.valueobject.Severidad;
import com.example.auditoria.domain.valueobject.TransicionInvalidaException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class HallazgoAuditoriaTest {

    private HallazgoAuditoria nuevo() {
        return new HallazgoAuditoria(HallazgoId.nuevo(), "Titulo", "Descripcion",
                "Infraestructura", Severidad.ALTA, LocalDate.of(2026, 8, 1));
    }

    private PlanRemediacion plan() {
        return new PlanRemediacion("Equipo de Infraestructura", LocalDate.of(2026, 8, 20), "Notas");
    }

    @Test
    void cicloCompletoDeEstados() {
        HallazgoAuditoria h = nuevo();
        assertEquals(EstadoHallazgo.ABIERTO, h.getEstado());

        assertEquals(EstadoHallazgo.ABIERTO, h.iniciarRemediacion(plan()));
        assertEquals(EstadoHallazgo.EN_REMEDIACION, h.getEstado());

        assertEquals(EstadoHallazgo.EN_REMEDIACION, h.cerrar());
        assertEquals(EstadoHallazgo.CERRADO, h.getEstado());
        assertNotNull(h.getFechaCierre());

        assertEquals(EstadoHallazgo.CERRADO, h.reabrir());
        assertEquals(EstadoHallazgo.REABIERTO, h.getEstado());
        assertNull(h.getFechaCierre());

        h.iniciarRemediacion(plan());
        assertEquals(EstadoHallazgo.EN_REMEDIACION, h.getEstado());
    }

    @Test
    void noSePuedeCerrarSinPlanDeRemediacion() {
        assertThrows(IllegalStateException.class, () -> nuevo().cerrar());
    }

    @Test
    void noSePuedeReabrirUnHallazgoQueSigueAbierto() {
        assertThrows(TransicionInvalidaException.class, () -> nuevo().reabrir());
    }

    @Test
    void noSePuedeIniciarRemediacionDosVeces() {
        HallazgoAuditoria h = nuevo();
        h.iniciarRemediacion(plan());
        assertThrows(TransicionInvalidaException.class, () -> h.iniciarRemediacion(plan()));
    }
}