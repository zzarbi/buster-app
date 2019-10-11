FROM python:3.7

WORKDIR /opt/app

COPY python/requirements.txt /opt/app/requirements.txt

RUN pip install -r requirements.txt
