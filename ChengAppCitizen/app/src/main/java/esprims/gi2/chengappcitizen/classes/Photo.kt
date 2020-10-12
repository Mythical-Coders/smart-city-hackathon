package esprims.gi2.chengappcitizen.classes

class Photo(var title:String, var image:ByteArray) {
    override fun toString(): String {
        return "Photo(title='$title', image=${image.contentToString()})"
    }
}