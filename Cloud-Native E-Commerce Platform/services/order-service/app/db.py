import os
import sqlite3


def db_path() -> str:
    return os.getenv("DB_PATH", "/data/orders.db")


def get_connection() -> sqlite3.Connection:
    conn = sqlite3.connect(db_path())
    conn.row_factory = sqlite3.Row
    return conn


def init_db() -> None:
    with get_connection() as conn:
        conn.execute(
            """
            CREATE TABLE IF NOT EXISTS orders (
                id TEXT PRIMARY KEY,
                customer_id TEXT NOT NULL,
                status TEXT NOT NULL,
                total_cents INTEGER NOT NULL,
                currency TEXT NOT NULL,
                created_at TEXT NOT NULL,
                updated_at TEXT NOT NULL
            )
            """
        )
        conn.execute(
            """
            CREATE TABLE IF NOT EXISTS order_items (
                order_id TEXT NOT NULL,
                product_id TEXT NOT NULL,
                name TEXT NOT NULL,
                quantity INTEGER NOT NULL,
                price_cents INTEGER NOT NULL,
                PRIMARY KEY (order_id, product_id)
            )
            """
        )
        conn.commit()
