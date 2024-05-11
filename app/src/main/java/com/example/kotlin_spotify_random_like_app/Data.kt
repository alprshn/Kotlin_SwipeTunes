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
