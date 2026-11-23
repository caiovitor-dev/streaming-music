
ALTER TABLE category_playlist
DROP CONSTRAINT category_playlist_category_id_fkey,
ADD CONSTRAINT category_playlist_category_id_fkey
FOREIGN KEY (category_id)
REFERENCES categories(id)
ON DELETE CASCADE;

ALTER TABLE category_playlist
DROP CONSTRAINT category_playlist_playlist_id_fkey,
ADD CONSTRAINT category_playlist_playlist_id_fkey
FOREIGN KEY (playlist_id)
REFERENCES playlists(id)
ON DELETE CASCADE;