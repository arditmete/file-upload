package com.logikcull.assignment.parser

import com.logikcull.assignment.model.LoadFileEntry
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.DisplayName
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import java.io.ByteArrayInputStream
import java.io.InputStreamReader

@WebMvcTest(LoadFileParser::class)
class LoadFileParserTest {
    @Test
    @DisplayName("Test parseCsv with valid CSV lines")
    fun parseCsvValid() {
        val csvContent =
                "IM,test-000001,S,0,@Import Test 01;IMAGES/001/;test-000001.tif;2,0\n" +
                "IM,test-000002,C,0,@Import Test 01;IMAGES/001/;test-000002.tif;2,0\n" +
                "IM,test-000003,D,0,@Import Test 01;IMAGES/001/;test-000003.tif;2,0"

        val result = LoadFileParser.parseCsv(InputStreamReader(ByteArrayInputStream(csvContent.toByteArray())))

        assertEquals(3, result.size)
        assertEquals(LoadFileEntry("test-000001", "Import Test 01", "IMAGES/001/test-000001.tif"), result[0])
        assertEquals(LoadFileEntry("test-000002", "Import Test 01", "IMAGES/001/test-000002.tif"), result[1])
    }

    @Test
    @DisplayName("Test parseXml with invalid XML structure")
    fun parseXmlInvalid() {
        val invalidXmlContent = "<invalid>Invalid XML content</invalid>"

            val result = LoadFileParser.parseXml(InputStreamReader(ByteArrayInputStream(invalidXmlContent.toByteArray())))

        assertEquals(0, result.size)
    }

    @Test
    @DisplayName("Test parseXml with valid XML lines")
    fun parseXmlValid() {
        val xmlContent = "<loadfile>\n" +
                "  <entries>\n" +
                "    <entry control-number=\"test-000001\">\n" +
                "        <volume>Import Test 01</volume>\n" +
                "        <image-path>IMAGES/001/</image-path>\n" +
                "        <image-name>test-000001.tif</image-name>\n" +
                "    </entry>\n" +
                "    <entry control-number=\"test-000002\">\n" +
                "        <volume>Import Test 01</volume>\n" +
                "        <image-path>IMAGES/001/</image-path>\n" +
                "        <image-name>test-000002.tif</image-name>\n" +
                "    </entry>\n" +
                "  </entries>\n" +
                "</loadfile>"

        val result = LoadFileParser.parseXml(InputStreamReader(ByteArrayInputStream(xmlContent.toByteArray())))

        assertEquals(2, result.size)
        assertEquals(LoadFileEntry("test-000001", "Import Test 01", "IMAGES/001/test-000001.tif"), result[0])
        assertEquals(LoadFileEntry("test-000002", "Import Test 01", "IMAGES/001/test-000002.tif"), result[1])
    }

    @Test
    @DisplayName("Test parseCsv with invalid CSV lines")
    fun parseCSVInvalid() {
        val csvContent =
                "IM,test-000001,S,0,@Import Test 01;IMAGES/001/;test-000001.tif;2,0\n" +
                "IM,test-000002,C,0,@Import Test 01;IMAGES/001/;test-000002.tif;2,0\n"

        val result = LoadFileParser.parseCsv(InputStreamReader(ByteArrayInputStream(csvContent.toByteArray())))

        assertEquals(2, result.size)
        assertNotEquals(LoadFileEntry("ABC-123456", "", ""), result[0])
        assertNotEquals(LoadFileEntry("DEF-654321", "", ""), result[1])
    }
}
