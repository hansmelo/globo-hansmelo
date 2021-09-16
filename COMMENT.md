## Comentarios

- Primeira ação que eu faria sobre essa task em um ambiente de trabalho seria discutir com o time com suporte da área de negócios, quais são as transições de estado esperadas para esse serviço, por exemplo se a inscrição está cancelada não pode ser comprada e sim apenas restartada. Basicamente seria a definição da máquina de estado da inscrição.

## Melhorias
- Adicionar mais testes de integração e unitários, exploraria possíveis edge cases.
- Adicionar mais tratamentos de erros e um possível retry mecanismo para caso de falhas recuperáveis.
- Avaliar e implementar utilizando a técnica de non-blocking IO presente no spring-reactor, para ganhos em quantos request a aplicação pode atender com menos recursos.
- Adicionar probes(Liveness and Readiness),métricas, dashboards e alertas mais significativos que auxiliem na manutenção da aplicação.
- Avaliar e aplicar boas praticas de configuracao do rabbitmq e do postgres para ambiente de producacao.
- Executar um teste de carga para ter melhor ideia de quanto aplicação está preparada para alta cargas.

