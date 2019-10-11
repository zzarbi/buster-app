from flask import Flask
import os
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)


DB_HOST = os.getenv('DB_HOST', 'buster-app-mysql')
DB_USER = os.getenv('DB_USER', 'dev')
DB_PASSWORD = os.getenv('DB_PASSWORD', 'password123')
DB_NAME = os.getenv('DB_NAME', 'buster_dev')
DB_PORT = int(os.getenv('DB_PORT', '3306'))

CONNECTION_URL = 'mysql://{}:{}@{}:{}/{}'.format(DB_USER, DB_PASSWORD, DB_HOST, DB_PORT, DB_NAME)

app.config['SQLALCHEMY_DATABASE_URI'] = CONNECTION_URL
db = SQLAlchemy(app)
