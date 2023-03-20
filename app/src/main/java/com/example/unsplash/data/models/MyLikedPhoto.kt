package com.example.unsplash.data.models

data class MyLikedPhoto (
    val alt_description: String,
    val blur_hash: String,
    val color: String,
    val created_at: String,
    val current_user_collections: List<Any>,
    val description: String,
    val height: Int,
    val id: String,
    val liked_by_user: Boolean,
    val likes: Int,
 //   val links: Links,
    val promoted_at: String,
    val sponsorship: Any,
 //   val topic_submissions: TopicSubmissions,
    val updated_at: String,
    val urls: Urls,
    val user: User,
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
    data class User(
        val accepted_tos: Boolean,
        val bio: String,
        val first_name: String,
        val for_hire: Boolean,
        val id: String,
        val instagram_username: String,
        val last_name: String,
 //       val links: LinksX,
        val location: String,
        val name: String,
        val portfolio_url: String,
 //       val profile_image: ProfileImage,
 //       val social: Social,
        val total_collections: Int,
        val total_likes: Int,
        val total_photos: Int,
        val twitter_username: String,
        val updated_at: String,
        val username: String
    )
}