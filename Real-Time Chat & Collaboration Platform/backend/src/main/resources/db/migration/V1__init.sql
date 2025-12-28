create table chat_rooms (
  id uuid primary key,
  name varchar(120) not null unique,
  created_at timestamp with time zone not null
);

create table chat_messages (
  id uuid primary key,
  room_id uuid not null references chat_rooms(id) on delete cascade,
  sender varchar(120) not null,
  content text not null,
  created_at timestamp with time zone not null
);

create index idx_chat_messages_room_created on chat_messages(room_id, created_at desc);
