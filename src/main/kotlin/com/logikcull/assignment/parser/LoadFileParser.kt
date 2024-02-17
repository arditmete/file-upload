package com.logikcull.assignment.parser

import com.logikcull.assignment.model.LoadFileEntry
import org.springframework.stereotype.Component
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

@Component
class LoadFileParser {
    companion object {
        fun parseCsv(loadFile: File): List<LoadFileEntry> {
            val entries = mutableListOf<LoadFileEntry>()
            loadFile.forEachLine { line ->
                val columns = line.split(",")
                val controlNumber = columns[1]
                val volumeAndPath = columns[4].substringAfter("@").split(";")
                val volume = volumeAndPath[0]
                val path = "${volumeAndPath[1]}${volumeAndPath[2]}"
                entries.add(LoadFileEntry(controlNumber, volume, path))
            }
            return entries
        }

        fun parseXml(loadFile: File): List<LoadFileEntry> {
            val entries = mutableListOf<LoadFileEntry>()
            val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
            val doc = docBuilder.parse(loadFile)
            val nodeList = doc.getElementsByTagName("entry")
            for (i in 0 until nodeList.length) {
                val node = nodeList.item(i)
                if (node.nodeType == org.w3c.dom.Node.ELEMENT_NODE) {
                    val element = node as org.w3c.dom.Element
                    val controlNumber = element.getAttribute("control-number")
                    val volume = element.getElementsByTagName("volume").item(0).textContent
                    val imagePath = element.getElementsByTagName("image-path").item(0).textContent
                    val imageName = element.getElementsByTagName("image-name").item(0).textContent
                    val path = "$imagePath$imageName"
                    entries.add(LoadFileEntry(controlNumber, volume, path))
                }
            }
            return entries
        }
    }
}
