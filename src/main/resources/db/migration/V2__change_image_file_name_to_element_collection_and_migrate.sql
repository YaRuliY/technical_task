CREATE TABLE fish_images
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    fish_id BIGINT       NOT NULL,
    images  VARCHAR(255) NOT NULL,
    FOREIGN KEY (fish_id) REFERENCES fish (id)
);

INSERT INTO fish_images (fish_id, image_file_name)
SELECT id, images
FROM fish;

ALTER TABLE fish DROP COLUMN image_file_name;