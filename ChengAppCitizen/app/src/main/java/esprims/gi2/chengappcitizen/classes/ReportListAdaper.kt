package esprims.gi2.chengappcitizen.classes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import esprims.gi2.chengappcitizen.R
import kotlinx.android.synthetic.main.activity_main.view.*

class ReportListAdapter(var context:Context, var photoList : ArrayList<Photo>):BaseAdapter() {

    private class ViewHolder(row: View?){
        var type : TextView
        var image :ImageView
        init {
            this.image = row?.findViewById(R.id.photoId) as ImageView
            this.type = row.findViewById(R.id.typeTextId) as TextView
        }
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
       val view : View?
       val viewHolder:ViewHolder
        if(convertView ==null){
            val layout = LayoutInflater.from(context)
            view = layout.inflate(R.layout.layout_report_list,parent,false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder

        }
        else{
            view = convertView
            viewHolder = view.tag as ViewHolder

        }

        val photo:Photo =  getItem(position) as Photo

        //convert image from byteArray to BitMap
        val convert= DbBitmapUtility(context)
        val imageBitmap = convert.getBitMap(photo.image)


        viewHolder.type.text = photo.title
        viewHolder.image.setImageBitmap(imageBitmap)




        return view as View
    }

    override fun getItem(position: Int): Any {
        return photoList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return photoList.count()
    }


}