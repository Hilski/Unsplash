package com.example.unsplash.data.models

data class CollectionDetailsPhoto (
    val alt_description: String,
    val blur_hash: String,
    val color: String,
    val created_at: String,
 //   val current_user_collections: List<Any>,
    val description: String,
    val downloads: Int,
//    val exif: Exif,
    val height: Int,
    val id: String,
    val liked_by_user: Boolean,
    val likes: Int,
//    val links: Links,
    val location: Location,
//    val meta: Meta,
    val promoted_at: String,
    val public_domain: Boolean,
//    val related_collections: RelatedCollections,
    val sponsorship: Any,
 //   val tags: List<TagX>,
//    val tags_preview: List<TagsPreview>,
//    val topic_submissions: TopicSubmissionsXXXX,
//    val topics: List<Any>,
    val updated_at: String,
    val urls: Urls,
//    val user: UserXXXXX,
    val views: Int,
    val width: Int
) {
    data class Urls(
        val full: String,
        val raw: String,
        val regular: String,
        val small: String,
        val small_s3: String,
        val thumb: String
    )
    data class Location(
        val city: String,
        val country: String,
        val position: Position
    ) {
        data class Position(
            val latitude: Double,
            val longitude: Double
        )
    }
}