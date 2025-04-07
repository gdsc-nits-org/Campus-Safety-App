package com.example.campussafetyapp.Developers

import DeveloperItem
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.campussafetyapp.R

class DeveloperAdapter(
    private val context: Context,
    private val items: List<DeveloperItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_DEVELOPER = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is DeveloperItem.SectionHeader -> TYPE_HEADER
            is DeveloperItem.Developer -> TYPE_DEVELOPER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.developer_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_developer, parent, false)
            DeveloperViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is DeveloperItem.SectionHeader -> (holder as HeaderViewHolder).bind(item)
            is DeveloperItem.Developer -> (holder as DeveloperViewHolder).bind(item, context)
        }
    }

    override fun getItemCount(): Int = items.size

    // ViewHolder for Developer
    class DeveloperViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val developerImage: ImageView = itemView.findViewById(R.id.developerImage)
        private val developerName: TextView = itemView.findViewById(R.id.developerName)
        private val developerBranch: TextView = itemView.findViewById(R.id.developerBranch)
        private val developerDetails: TextView = itemView.findViewById(R.id.developerDetails)
        private val cardView: CardView = itemView.findViewById(R.id.cardView)

        fun bind(developer: DeveloperItem.Developer, context: Context) {
            developerName.text = developer.name
            developerBranch.text = developer.branch
            developerDetails.text = developer.details

            // ✅ Load image using Glide
            Glide.with(context)
                .load(developer.imageResId)
                .placeholder(R.drawable.placeholder) // Optional: show while loading
                .into(developerImage)

            itemView.setOnClickListener {
                showSocialMediaDialog(context, developer)
            }
        }

        private fun showSocialMediaDialog(context: Context, developer: DeveloperItem.Developer) {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_social_links, null)
            val builder = AlertDialog.Builder(context).setView(dialogView)
            val dialog = builder.create()

            val btnFacebook: ImageView = dialogView.findViewById(R.id.btnFacebook)
            val btnInstagram: ImageView = dialogView.findViewById(R.id.btnInstagram)
            val btnLinkedIn: ImageView = dialogView.findViewById(R.id.btnLinkedIn)
            val btnGitHub: ImageView = dialogView.findViewById(R.id.btnGitHub)

            btnFacebook.setOnClickListener { openLink(context, developer.facebookLink) }
            btnInstagram.setOnClickListener { openLink(context, developer.instagramLink) }
            btnLinkedIn.setOnClickListener { openLink(context, developer.linkedInLink) }
            btnGitHub.setOnClickListener { openLink(context, developer.gitHubLink) }

            dialog.show()
        }

        private fun openLink(context: Context, url: String) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }
    }

    // ViewHolder for Section Headers
    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val sectionTitle: TextView = itemView.findViewById(R.id.sectionTitle)

        fun bind(header: DeveloperItem.SectionHeader) {
            sectionTitle.text = header.title
        }
    }
}
