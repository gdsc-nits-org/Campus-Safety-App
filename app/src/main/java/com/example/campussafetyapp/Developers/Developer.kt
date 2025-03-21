sealed class DeveloperItem {
    data class Developer(
        val name: String,
        val branch: String,
        val imageResId: Int,
        val details: String,
        val facebookLink: String,
        val instagramLink: String,
        val linkedInLink: String,
        val gitHubLink: String
    ) : DeveloperItem()

    data class SectionHeader(val title: String) : DeveloperItem()
}
