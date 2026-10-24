package com.translate.keyboard.emoji

object EmojiRepository {

    data class EmojiCategory(val name: String, val emojis: List<String>)

    val categories = listOf(
        EmojiCategory("Smileys", listOf("😀", "😃", "😄", "😁", "😆", "😅", "😂", "🤣", "😊", "😇", "🙂", "🙃", "😉", "😌", "😍", "🥰", "😘", "😗", "😙", "😚", "😋", "😛", "😝", "😜", "🤪", "🤨", "🧐", "🤓", "😎", "🤩", "🥳", "😏", "😒", "😞", "😔", "😟", "😕", "🙁", "😣", "ㅜ", "😭", "😤", "😡", "🤬")),
        EmojiCategory("People", listOf("👋", "🤚", "🖐️", "✋", "🖖", "👌", "🤌", "🤏", "✌️", "🤞", "🫰", "🤟", "🤘", "🤙", "👈", "👉", "👆", "🖕", "👇", "☝️", "👍", "👎", "✊", "👊", "🤛", "🤜", "👏", "🙌", "👐", "🤲", "🤝", "🙏", "✍️", "💅", "🤳", "💪", "🦾", "🦵", "🦿", "👣", "👀", "👁️", "舌", "👄", "💋", "부", "👤", "👥", "🗣️")),
        EmojiCategory("Animals", listOf("🐶", "🐱", "🐭", "🐹", "🐰", "🦊", "🐻", "🐼", "🐨", "🐯", "🦁", "🐮", "🐷", "🐽", "🐸", "🐵", "🙈", "🙉", "🙊", "🐒", "🐔", "🐧", "🐦", "🐤", "🐣", "🐥", "🦆", "🦅", "🦉", "🦇", "🐺", "🐗", "🐴", "🦄", "🐝", "🪱", "🐛", "🦋", "🐌", "🐞", "🐜", "🦟", "🦗", "🕷️", "🕸️", "🦂", "🐢", "🐍", "🦎", "🦖", "🦕", "🐙", "🦑", "🦐", "🦞", "🦀", "🐡", "🐠", "🐟", "🐬", "🐳", "🐋", "🦈", "鳄", "🐊", "🐅", "🐆", "🦓", "🦍", "🦧", "🐘", "🦛", "🦏", "🐪", "🐫", "🦒", "캥거루", "🦘", "🐃", "🐂", "🐄", "🐎", "🐖", "🐏", "🐑", " llamas", " llamas", "🦙", "🐐", "🦌", "🐕", "🐩", "🦮", "🐕‍🦺", "🐈", "🐈‍⬛", "feather", "🪶", "🐓", "🦃", "🦤", "🦚", "🦜", "🦢", "🦩", "🕊️", "🐇", "🦝", "skunk", "🦨", "🦡", "🦫", "otter", "🦭", "슬프", "🐾", "🐉", "🐲")),
        EmojiCategory("Food", listOf("🍏", "🍎", "🍐", "🍊", "🍋", "🍌", "🍉", "🍇", "🍓", "🫐", "🍈", "🍒", "🍑", "🥭", "🍍", "🥥", "🥝", "🍅", "🍆", "🥑", "🥦", "🥬", "🥒", "🌶️", "🫑", "🌽", "🥕", "🫒", "🧄", "🧅", "🥔", "🍠", "🥐", "🥯", "🍞", "🥖", "🥨", "🧀", "🥚", "🍳", "バター", "🥞", "🧇", "🥓", "🥩", "🍗", "🍖", "🦴", "핫도그", "🍔", "🍟", "🍕", "🥪", "pita", "🌮", "🌯", "tamale", "🥙", "🧆", "🥘", "🍲", "fondue", "bowl", "🥗", "팝콘", "🧈", "🧂", "🥫", "bento", "rice", "curry", "sushi", "spaghetti", "sweet", "dango", "dumpling", "fortune", "takeout", "oyster", "steamed", "oden", "sushi", "fried", "moon", "dango", "coffee", "tea", "boba", "teacup", "sake", "bottle", "wine", "cocktail", "tropical", "beer", "clinking", "tumbler", "cup", "straw", "bubble", "beverage", "mate", "ice", "fork", "knife", "spoon", "bowl", "takeout")),
        EmojiCategory("Activities", listOf("⚽", "🏀", "🏈", "⚾", "🥎", "🎾", "🏐", "🏉", "🥏", "🎱", "🪀", "🏓", "🏸", "🏒", "🏑", "🥍", "🏏", "🪃", "🥅", "⛳", "🪁", "🏹", "🎣", "🤿", "🥊", "🥋", "🎽", "🛹", "ローラ", "🛼", "sled", "⛸️", "curling", "ski", "snowboard", "parachute", "wrestling", "water", "gymnastics", "fencing", "handball", "golf", "ice", "surfing", "swimming", "basketball", "volleyball", "trophy", "medal", "military", "ticket", "performing", "art", "circus", "tent", "clapper", "film", "computer", "game", "dart", "billiards", "puzzle", "teddy", "pinata", "mirror", "nesting", "spool", "yarn", "knot")),
        EmojiCategory("Symbols", listOf("❤️", "🧡", "💛", "💚", "💙", "💜", "🖤", "🤍", "🤎", "💔", "❤️‍🔥", "❤️‍🩹", "💕", "💞", "💓", "💗", "💖", "💘", "💝", "💟", "☮️", "✝️", "☪️", "🕉️", "☸️", "✡️", "🔯", "🕎", "☯️", "☦️", "🛐", "⛎", "♈", "♉", "♊", "♋", "♌", "♍", "♎", "♏", "♐", "♑", "♒", "♓", "🆔", "⚛️", "🉑", "☢️", "☣️", "📴", "📳", "🈶", "🈚", "🈸", "🈺", "🈷️", "✴️", "🆚", "💮", "🉐", "㊙️", "㊗️", "🈴", "🈵", "🈹", "🈲", "🅰️", "🅱️", "🆎", "🆑", "🅾️", "🆘", "❌", "⭕", "🛑", "📛", "🚫", "💯", "💢", "♨️", "🚷", "🚯", "🚳", "🚱", "🔞", "📵", "🚭", "❗", "❓", "❕", "❔", "💯", "⚠️", "🚸", "🔱", "⚜️", "🔰", "♻️", "✅", "🟢", "🟡", "🟠", "🔴", "🟤", "🟣", "🔵", "🔷", "🔶", "🔸", "🔹", "🔺", "🔻", "💠", "🔘", "🔳", "🔲", "🏁", "🚩", "🎌", "🏴", "🏳️"))
    )

    fun searchEmojis(query: String): List<String> {
        val q = query.lowercase()
        val results = mutableListOf<String>()
        // Keyword mapping for Arabic & English
        val map = mapOf(
            "love" to listOf("❤️", "😍", "🥰", "💖", "💕", "💘"),
            "حب" to listOf("❤️", "😍", "🥰", "💖", "💕"),
            "laugh" to listOf("😂", "🤣", "😆"),
            "ضحك" to listOf("😂", "🤣", "😆"),
            "sad" to listOf("😢", "😭", "😞"),
            "حزين" to listOf("😢", "😭", "😞"),
            "fire" to listOf("🔥"),
            "نار" to listOf("🔥"),
            "happy" to listOf("😀", "😊", "🥳"),
            "سعيد" to listOf("😀", "😊", "🥳")
        )

        if (map.containsKey(q)) {
            return map[q]!!
        }

        for (cat in categories) {
            for (emoji in cat.emojis) {
                if (emoji.contains(query)) {
                    results.add(emoji)
                }
            }
        }
        if (results.isEmpty() && categories.isNotEmpty()) {
            return categories[0].emojis.take(15)
        }
        return results
    }
}
