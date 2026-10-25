package com.example.auditoria.config;

import com.example.auditoria.usecase.CerrarHallazgoUseCase;
import com.example.auditoria.usecase.ConsultarHallazgoUseCase;
import com.example.auditoria.usecase.ConsultarHistorialUseCase;
import com.example.auditoria.usecase.IniciarRemediacionUseCase;
import com.example.auditoria.usecase.ObtenerDashboardAuditoriaUseCase;
import com.example.auditoria.usecase.ReabrirHallazgoUseCase;
import com.example.auditoria.usecase.RegistrarHallazgoUseCase;
import com.example.auditoria.usecase.impl.CerrarHallazgoService;
import com.example.auditoria.usecase.impl.ConsultarHallazgoService;
import com.example.auditoria.usecase.impl.ConsultarHistorialService;
import com.example.auditoria.usecase.impl.IniciarRemediacionService;
import com.example.auditoria.usecase.impl.ObtenerDashboardAuditoriaService;
import com.example.auditoria.usecase.impl.ReabrirHallazgoService;
import com.example.auditoria.usecase.impl.RegistrarHallazgoService;
import com.example.auditoria.usecase.port.HallazgoRepositoryPort;
import com.example.auditoria.usecase.port.HistorialAuditoriaPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

@Configuration
public class AuditoriaConfiguration {

    @Bean
    public RegistrarHallazgoUseCase registrarHallazgoUseCase(HallazgoRepositoryPort repo, TransactionTemplate tx) {
        return transaccional(RegistrarHallazgoUseCase.class, new RegistrarHallazgoService(repo), tx);
    }

    @Bean
    public IniciarRemediacionUseCase iniciarRemediacionUseCase(HallazgoRepositoryPort repo,
                                                               HistorialAuditoriaPort historial, TransactionTemplate tx) {
        return transaccional(IniciarRemediacionUseCase.class, new IniciarRemediacionService(repo, historial), tx);
    }

    @Bean
    public CerrarHallazgoUseCase cerrarHallazgoUseCase(HallazgoRepositoryPort repo,
                                                       HistorialAuditoriaPort historial, TransactionTemplate tx) {
        return transaccional(CerrarHallazgoUseCase.class, new CerrarHallazgoService(repo, historial), tx);
    }

    @Bean
    public ReabrirHallazgoUseCase reabrirHallazgoUseCase(HallazgoRepositoryPort repo,
                                                         HistorialAuditoriaPort historial, TransactionTemplate tx) {
        return transaccional(ReabrirHallazgoUseCase.class, new ReabrirHallazgoService(repo, historial), tx);
    }

    @Bean
    public ConsultarHallazgoUseCase consultarHallazgoUseCase(HallazgoRepositoryPort repo) {
        return new ConsultarHallazgoService(repo);
    }

    @Bean
    public ObtenerDashboardAuditoriaUseCase obtenerDashboardAuditoriaUseCase(HallazgoRepositoryPort repo) {
        return new ObtenerDashboardAuditoriaService(repo);
    }

    @Bean
    public ConsultarHistorialUseCase consultarHistorialUseCase(HallazgoRepositoryPort repo,
                                                               HistorialAuditoriaPort historial) {
        return new ConsultarHistorialService(repo, historial);
    }

    // Ejecuta cada llamada al caso de uso dentro de UNA transaccion, sin anotar la capa usecase.
    private static <T> T transaccional(Class<T> contrato, T objetivo, TransactionTemplate tx) {
        InvocationHandler handler = (proxy, metodo, args) -> tx.execute(status -> {
            try {
                return metodo.invoke(objetivo, args);
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof RuntimeException re) throw re;
                throw new IllegalStateException(e.getCause());
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        });
        return contrato.cast(Proxy.newProxyInstance(
                contrato.getClassLoader(), new Class<?>[]{contrato}, handler));
    }
}