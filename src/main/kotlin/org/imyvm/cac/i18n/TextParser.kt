package org.imyvm.cac.i18n

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

object TextParser {

    private val legacy = LegacyComponentSerializer.legacySection()

    fun parse(raw: String): Component {
        val lines = raw.split("\n")
        var result = Component.empty()

        for ((index, line) in lines.withIndex()) {
            val parsedLine = when {
                line.contains('&') -> parseAmpersandColors(line)
                line.contains('§') -> parseSectionColors(line)
                else -> Component.text(line)
            }
            result = result.append(parsedLine)
            if (index < lines.lastIndex) result = result.append(Component.text("\n"))
        }

        return result
    }

    private fun parseAmpersandColors(raw: String): Component {
        val sectioned = raw.replace("&([0-9a-fk-or])".toRegex(RegexOption.IGNORE_CASE), "§$1")
        return parseSectionColors(sectioned)
    }

    private fun parseSectionColors(sectioned: String): Component =
        legacy.deserialize(sectioned)
}

