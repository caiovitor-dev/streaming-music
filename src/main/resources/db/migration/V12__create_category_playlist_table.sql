CREATE TABLE category_playlist(

category_id UUID NOT NULL REFERENCES categories(id),
playlist_id UUID NOT NULL REFERENCES playlists(id),
created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

PRIMARY KEY (category_id,playlist_id)

)