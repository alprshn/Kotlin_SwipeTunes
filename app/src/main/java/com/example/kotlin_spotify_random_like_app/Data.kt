data class Playlist(
    val collaborative: Boolean,
    val description: String,
    val external_urls: ExternalUrls,
    val followers: Followers,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val owner: Owner
)

data class ExternalUrls(
    val spotify: String
)

data class Followers(
    val href: Any?,
    val total: Int
)

data class Image(
    val url: String,
    val height: Any?, // Height ve width nullable olabilir
    val width: Any?
)

data class Owner(
    val external_urls: ExternalUrls,
    val href: String,
    val id: String,
    val type: String,
    val uri: String,
    val display_name: String
)

data class PlayRequest(
    val context_uri: String,
    val offset: Offset,
    val position_ms: Int
)

data class Offset(
    val position: Int
)



data class AlbumsResponse(
    val albums: Albums
)


data class Albums(
    val href: String,
    val items: List<AlbumItem>,
    val limit: Int,
    val next: String?,
    val offset: Int,
    val previous: String?,
    val total: Int
)

data class AlbumItem(
    val album_type: String,
    val total_tracks: Int,
    val available_markets: List<String>,
    val external_urls: ExternalUrls,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val release_date: String,
    val release_date_precision: String,
    val type: String,
    val uri: String,
    //val artists: List<Artist>
)

data class PlaylistRequest(
    val name: String,
    val description: String,
    val public: Boolean
)