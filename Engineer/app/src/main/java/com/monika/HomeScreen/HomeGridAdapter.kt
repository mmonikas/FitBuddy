package com.monika.HomeScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.view.marginBottom
import androidx.core.view.marginRight
import androidx.recyclerview.widget.RecyclerView
import com.monika.Enums.MenuItemType
import com.monika.HomeFragmentDelegate
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
        holder.itemView.homegridmenu_cardTitle.text = menuItems[position].name?.name
        holder.itemView.setOnClickListener {
            menuItems[position].name?.let {
                menuItemType ->
                homeFragmentDelegate?.gridMenuItemTriggered(atIndex = position, withId = menuItemType)
            }
        }
    }
}
