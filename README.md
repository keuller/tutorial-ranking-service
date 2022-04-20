# tutorial-ranking-service
Ranking Service

## Cloning this Repo

```shell
git clone https://github.com/keuller/tutorial-ranking-service.git
```

## Start Postgres
```
docker run --name=pgdb --hostname=postgresql \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 postgres:latest 
```

## Run locally
```shell
./gradlew run
```

## Endpoints
```
| Path               | Verb  | Description            |
|--------------------|-------|------------------------|
| /                  | GET   | Default endpoint       | 
| /v1/companies      | GET   | List all companies     |
| /v1/companies      | POST  | Register a new company |
| /v1/companies/vote | PATCH | Apply vote operation   |
```
