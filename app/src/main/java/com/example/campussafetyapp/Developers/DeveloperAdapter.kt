package com.example.campussafetyapp.Developers

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
import com.example.campussafetyapp.R

class DeveloperAdapter(
    private val context: Context,
    private val developers: List<Developer>
) : RecyclerView.Adapter<DeveloperAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val developerImage: ImageView = itemView.findViewById(R.id.developerImage)
        val developerName: TextView = itemView.findViewById(R.id.developerName)
        val developerBranch: TextView = itemView.findViewById(R.id.developerBranch)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_developer, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val developer = developers[position]
        holder.developerName.text = developer.name
        holder.developerBranch.text = developer.branch
        holder.developerImage.setImageResource(developer.imageResId)

        // Handle Click: Open Dialog with Social Media Links
        holder.itemView.setOnClickListener {
            showSocialMediaDialog(holder.itemView.context, developer)
        }
    }

    override fun getItemCount(): Int = developers.size

    private fun showSocialMediaDialog(context: Context, developer: Developer) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_social_links, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)

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
