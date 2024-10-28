package com.github.ngodat0103.yamp.gateway.security.filter;

import io.opentelemetry.api.trace.Span;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@AllArgsConstructor
@Slf4j
public class AddTraceIdFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String correlationId = getTraceParent();
        if(correlationId == null) {
            log.debug("Correlation ID is null");
            exchange.getResponse().getHeaders().add("X-TraceId", "null");
        }
        else {
            log.debug("Correlation ID is {}", correlationId);
            exchange.getResponse().getHeaders().add("X-TraceId", correlationId);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
    private String getTraceParent() {
        Span currentSpan = Span.current();
        if (Objects.isNull(currentSpan)) {
            return null;
        }
        return currentSpan.getSpanContext().getTraceId();
    }
}
