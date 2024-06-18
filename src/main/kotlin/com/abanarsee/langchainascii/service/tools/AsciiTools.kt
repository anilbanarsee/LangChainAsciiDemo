package com.abanarsee.langchainascii.service.tools

import dev.langchain4j.agent.tool.P
import dev.langchain4j.agent.tool.Tool
import java.awt.Color
import java.awt.Font
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import java.io.File
import java.util.stream.Collectors
import java.util.stream.IntStream
import javax.imageio.ImageIO
import kotlin.math.ln
import kotlin.math.roundToInt

class AsciiTools {

    @Tool("Convert human years into dog years")
    fun convertHumanYearsIntoDogYears(
        @P("The age in human years to convert to dog years") humanAge: Int
    ): Int {
        return ((16 * ln(humanAge.toDouble())) + 31).roundToInt()
     }

    @Tool("Convert a message or text into ascii text")
    fun convertToAsciiText(@P("The text to convert into ascii text") text: String): String {
        val formattedText = text.breakLines();
        val img = BufferedImage(320, 200, BufferedImage.TYPE_INT_ARGB)
        val g2d = img.createGraphics()

        g2d.paint = Color.BLACK
        g2d.font = Font("SansSerif", Font.BOLD, 40)
        val fm = g2d.fontMetrics
        formattedText.split("\n").forEachIndexed { index, line ->
            val x = 10
            val y = (75 + (index * 100)) / 2
            g2d.drawString(line, x, y);
        }

        g2d.dispose()
        var scaleFactorX = 0.6;
        var scaleFactorY = 0.6;
        var scaled = AffineTransform()
            .apply { scale(scaleFactorX, scaleFactorY) }
            .let { transform -> AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC ) }
            .let { operation -> operation.filter(img,
                BufferedImage((img.width*scaleFactorX).toInt(), (img.height*scaleFactorY).toInt(), BufferedImage.TYPE_INT_ARGB))}


        val ascii = IntStream.range(0, scaled.height).boxed().map { y ->
            IntStream.range(0, scaled.width).collect(
                    { StringBuilder() },
                    { s, x -> s.append(scaled.getAsciiOf(x, y))},
                    { s1, s2 -> s1.append(s2) }
                ).toString()
        }.collect(Collectors.joining("\n"));


        val outputFile = File("./out/image.png")
        ImageIO.write(img, "png", outputFile)
        val outputFileScaled = File("./out/imagescaled.png")
        ImageIO.write(scaled, "png", outputFileScaled)
        return ascii;
    }

    private fun String.breakLines(): String {
        val total = StringBuilder()
        val builder = StringBuilder()
        this.split(" ").forEach {
            if(builder.length + it.length > 19) {
                total.append(builder.toString())
                total.append("\n")
                builder.clear()
            }
            builder.append("$it ")
        }
        total.append(builder.toString())
        return total.toString()
    }

    private fun BufferedImage.getAsciiOf(x: Int, y: Int): String {
        val alpha = (getRGB(x, y) ushr 24)
        return when {
            alpha<25 -> " "
            alpha<100 -> "."
            alpha<150 -> "+"
//            alpha<230 -> ""
            else -> "#"
        }
    }
}
