package net.albertogarrido.rickandmorty.ui.characterfinder

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_character.view.*
import net.albertogarrido.rickandmorty.R

import net.albertogarrido.rickandmorty.network.RickAndMortyCharacter

class CharacterAdapter : PagedListAdapter<RickAndMortyCharacter, RecyclerView.ViewHolder>(diffCallback) {

    companion object {
        private val diffCallback = DiffCallback()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as CharacterViewHolder).bind(getItem(position)!!)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CharacterViewHolder {
        val itemView: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_character, viewGroup, false)
        return CharacterViewHolder(itemView)
    }

    class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val charAvatar: ImageView = view.charAvatar
        private val charName: TextView = view.charName
        private val charStatus: TextView = view.charStatus

        fun bind(character: RickAndMortyCharacter) {
            charName.text = character.name
            charStatus.text = character.status
            Picasso.get().load(character.avatar).into(charAvatar)
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<RickAndMortyCharacter>() {

        override fun areItemsTheSame(
                oldItem: RickAndMortyCharacter, newItem: RickAndMortyCharacter): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
                oldItem: RickAndMortyCharacter, newItem: RickAndMortyCharacter): Boolean = oldItem == newItem

    }
}