
```bash
cd docker 
docker compose up -d
```

```bash
ant run
```


# OTEL Backend
`docker run -p 8000:8000 -p 4318:4318 -p 4317:4317 -p 8080:8080 -p 8002:8002 hyperdx/hyperdx-local`

https://www.hyperdx.io/docs/install/java

```
export OTEL_EXPORTER_OTLP_ENDPOINT=https://in-otel.hyperdx.io
export OTEL_EXPORTER_OTLP_HEADERS=authorization=your_hyperdx_api_key
```

http://localhost:8080/team
```bash
export OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4318 \
export OTEL_EXPORTER_OTLP_HEADERS='authorization=52a2ce46-3cad-468f-8d7d-844d73c72133' \
export OTEL_EXPORTER_OTLP_PROTOCOL=http/protobuf \
export OTEL_LOGS_EXPORTER=otlp \
export OTEL_SERVICE_NAME='java-prototype'
```