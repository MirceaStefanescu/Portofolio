import os
import sqlite3


def db_path() -> str:
    return os.getenv("DB_PATH", "/data/payments.db")


def get_connection() -> sqlite3.Connection:
    conn = sqlite3.connect(db_path())
    conn.row_factory = sqlite3.Row
    return conn


def init_db() -> None:
    with get_connection() as conn:
        conn.execute(
            """
            CREATE TABLE IF NOT EXISTS payments (
                id TEXT PRIMARY KEY,
                order_id TEXT NOT NULL,
                status TEXT NOT NULL,
                amount_cents INTEGER NOT NULL,
                currency TEXT NOT NULL,
                created_at TEXT NOT NULL
            )
            """
        )
        conn.commit()
