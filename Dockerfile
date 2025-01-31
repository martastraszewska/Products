FROM ubuntu:latest
LABEL authors="marta"

ENTRYPOINT ["top", "-b"]