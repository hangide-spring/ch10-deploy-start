INSERT INTO user_tb (username, password, created_at) VALUES ('ssar', '$2y$10$F3APmVkmIcJbYTstu8zQoeOE07l0ZitVq5ffonEHo1WoPKH982QWq', now());
INSERT INTO user_tb (username, password, created_at) VALUES ('cos', '$2y$10$F3APmVkmIcJbYTstu8zQoeOE07l0ZitVq5ffonEHo1WoPKH982QWq', now());

INSERT INTO board_tb (title, content, created_at, user_id) VALUES ('제목1', '내용1', now(), 1);
INSERT INTO board_tb (title, content, created_at, user_id) VALUES ('제목2', '내용2', now(), 1);
INSERT INTO board_tb (title, content, created_at, user_id) VALUES ('제목3', '내용3', now(), 2);