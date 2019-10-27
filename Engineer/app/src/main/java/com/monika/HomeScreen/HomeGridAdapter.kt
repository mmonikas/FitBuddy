package com.monika.HomeScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.monika.Enums.MenuItemType
import com.monika.Enums.MenuItemType.*
import com.monika.Model.Home.HomeMenuItem
import com.monika.R
import com.monika.Services.Utils
import kotlinx.android.synthetic.main.cardview_homegrid_menu.view.*

class HomeGridAdapter(private val menuItems: Array<HomeMenuItem>): RecyclerView.Adapter<HomeGridAdapter.HomeGridViewHolder>() {

    var homeFragmentDelegate: HomeFragmentDelegate? = null

    class HomeGridViewHolder(cardView: CardView): RecyclerView.ViewHolder(cardView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeGridViewHolder {val cardView = LayoutInflater.from(parent.context).inflate(
        R.layout.cardview_homegrid_menu, parent, false) as CardView
        //TODO set view size margins etc
        cardView.layoutParams.width = (parent.width - Utils.getValueInDP(parent.context, 48)) / 2
        cardView.layoutParams.height = parent.height / 4
      //  cardView.setContentPadding(2, 2, 2, 2)
        return HomeGridViewHolder(cardView)

    }

    override fun getItemCount(): Int {
        return menuItems.size
    }

    override fun onBindViewHolder(holder: HomeGridViewHolder, position: Int) {
        holder.itemView.homegridmenu_cardTitle.text = menuItems[position].name?.name?.capitalize()
        setImage(holder.itemView.homegridmenu_cardImage, menuItems[position].name)
        holder.itemView.setOnClickListener {
            menuItems[position].name?.let {
                menuItemType ->
                homeFragmentDelegate?.gridMenuItemTriggered(atIndex = position, withId = menuItemType)
            }
        }
    }

    private fun setImage(homegridmenu_cardImage: ImageView?, name: MenuItemType?) {
        when (name) {
            ćwiczenia ->  homegridmenu_cardImage?.setImageResource(R.drawable.ic_fitness_center_black_24dp)
            kalendarz ->  homegridmenu_cardImage?.setImageResource(R.drawable.icon_calendar)
            treningi ->  homegridmenu_cardImage?.setImageResource(R.drawable.ic_assignment_black_24dp)
            przyjaciele ->  homegridmenu_cardImage?.setImageResource(R.drawable.ic_people_black_24dp)
            profil ->  homegridmenu_cardImage?.setImageResource(R.drawable.ic_person_black_24dp)
            ustawienia ->  homegridmenu_cardImage?.setImageResource(R.drawable.ic_menu_manage)
        }
    }
}
