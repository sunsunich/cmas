package org.cmas.backend;


import com.google.zxing.WriterException;
import com.mortennobel.imagescaling.ResampleOp;
import org.cmas.MockUtil;
import org.cmas.entities.PersonalCard;
import org.cmas.entities.diver.Diver;
import org.cmas.util.barcode.BarcodeEncoder;
import org.cmas.util.barcode.Pixels;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;

/**
 * Created on Dec 14, 2015
 *
 * @author Alexander Petukhov
 */
public class DrawCardServiceImpl implements DrawCardService {

    private static final float QR_SCALE_FACTOR = 136.0f / 640.0f;
    private static final float QR_X = 480.0f / 640.0f;
    private static final float QR_Y = 69.0f / 414.0f;

    private static final int CARD_NUMBER_FONT_SIZE = 56;
    private static final float CARD_NUMBER_Y = 350.0f / 414.0f;
    private static final float CARD_NUMBER_X_1 = 80.0f / 640.0f;
    private static final float CARD_NUMBER_X_2 = 200.0f / 640.0f;
    private static final float CARD_NUMBER_X_3 = 320.0f / 640.0f;
    private static final float CARD_NUMBER_X_4 = 440.0f / 640.0f;

    private static final int NAME_FONT_SIZE = 22;
    private static final float NAME_X = 468.0f / 640.0f;
    private static final float FIRST_NAME_Y = 90.0f / 414.0f;
    private static final float LAST_NAME_Y = 115.0f / 414.0f;
    private static final float DIVER_TYPE_Y = 140.0f / 414.0f;

    private static final float STAR_SCALE_FACTOR = 40.0f / 414.0f;
    private static final float STAR_Y = 140.0f / 414.0f;
    private static final float STAR_X_1 = 280.0f / 640.0f;
    private static final float STAR_X_2 = 340.0f / 640.0f;
    private static final float STAR_X_3 = 400.0f / 640.0f;

    @SuppressWarnings("OverlyLongMethod")
    @Override
    public BufferedImage drawDiverCard(PersonalCard card) throws WriterException, IOException {
        BufferedImage initImage = ImageIO.read(getClass().getResourceAsStream("cmas_card.png"));
        int width = initImage.getWidth();
        int height = initImage.getHeight();
        BufferedImage finalImage = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = finalImage.createGraphics();
        g2d.drawImage(initImage, 0, 0, null);

        g2d.setPaint(Color.BLACK);
        g2d.setFont(new Font("Serif", Font.BOLD, CARD_NUMBER_FONT_SIZE));

        String cardNumber = card.getNumber();
        String cardNumber1 = cardNumber.substring(0, 4);
        g2d.drawString(
                cardNumber1,
                CARD_NUMBER_X_1 * (float) width,
                CARD_NUMBER_Y * (float) height
        );
        String cardNumber2 = cardNumber.substring(4, 8);
        g2d.drawString(
                cardNumber2,
                CARD_NUMBER_X_2 * (float) width,
                CARD_NUMBER_Y * (float) height
        );
        String cardNumber3 = cardNumber.substring(8, 12);
        g2d.drawString(
                cardNumber3,
                CARD_NUMBER_X_3 * (float) width,
                CARD_NUMBER_Y * (float) height
        );
        String cardNumber4 = cardNumber.substring(12);
        g2d.drawString(
                cardNumber4,
                CARD_NUMBER_X_4 * (float) width,
                CARD_NUMBER_Y * (float) height
        );

        g2d.setPaint(new Color(0x25456c));
        g2d.setFont(new Font("Serif", Font.BOLD, NAME_FONT_SIZE));

        float x = NAME_X * (float) width;
        Diver diver = card.getDiver();
        drawWithRightAlign(g2d, diver.getFirstName(), x, FIRST_NAME_Y * (float) height);
        drawWithRightAlign(g2d, diver.getLastName(), x, LAST_NAME_Y * (float) height);
        String diverTypeStr = "";
        switch (diver.getDiverType()) {
            case DIVER:
                diverTypeStr = "DIVER";
                break;
            case INSTRUCTOR:
                diverTypeStr = "INSTRUCTOR";
                break;
        }
        drawWithRightAlign(g2d, diverTypeStr, x, DIVER_TYPE_Y * (float) height);
        @SuppressWarnings("NumericCastThatLosesPrecision")
        int qrSize = (int) ((float) width * QR_SCALE_FACTOR);
        Pixels qrCode = BarcodeEncoder.createQRCode(
                cardNumber, qrSize, qrSize
        );
        BufferedImage qrCodeImage = new BufferedImage(qrCode.width, qrCode.height, BufferedImage.TYPE_INT_RGB);
        qrCodeImage.setRGB(0, 0, qrCode.width, qrCode.height, qrCode.pixels, 0, qrCode.width);
        //noinspection NumericCastThatLosesPrecision
        g2d.drawImage(qrCodeImage, (int) (QR_X * (float) width), (int) (QR_Y * (float) height), null);

        if (diver.getDiverLevel() != null) {
            //noinspection NumericCastThatLosesPrecision
            BufferedImage starImage = ImageIO.read(getClass().getResourceAsStream("star.png"));
            switch (card.getDiverLevel()) {
                case THREE_STAR:
                    //noinspection NumericCastThatLosesPrecision
                    drawStar(width, height, g2d, starImage, (int) (STAR_X_3 * (float) width));
                    //fall through
                case TWO_STAR:
                    //noinspection NumericCastThatLosesPrecision
                    drawStar(width, height, g2d, starImage, (int) (STAR_X_2 * (float) width));
                    //fall through
                case ONE_STAR:
                    //noinspection NumericCastThatLosesPrecision
                    drawStar(width, height, g2d, starImage, (int) (STAR_X_1 * (float) width));
                    break;
            }
        }
        g2d.dispose();
        return finalImage;
    }

    @SuppressWarnings("NumericCastThatLosesPrecision")
    private static void drawStar(int width, int height, Graphics2D g2d, BufferedImage starImage, int x) {
        int starSize = (int) ((float) width * STAR_SCALE_FACTOR);
        BufferedImage after = new BufferedImage(starSize, starSize, BufferedImage.TYPE_INT_ARGB);
        BufferedImageOp resampleOp = new ResampleOp(starSize, starSize);
        resampleOp.filter(starImage, after);
        g2d.drawImage(after, x, (int) (STAR_Y * (float) height), null);
    }

    private static void drawWithRightAlign(Graphics2D g2d, String text, float x, float y) {
        int actualWidth = g2d.getFontMetrics().stringWidth(text);
        float finalX = x - (float) actualWidth;
        g2d.drawString(text, finalX, y);
    }

    public static void main(String[] args) {
        try {
            BufferedImage image = new DrawCardServiceImpl().drawDiverCard(MockUtil.getDiver().getPrimaryPersonalCard());
            ImageIO.write(image, "png", new File("/Users/sunsunich/workplace/сmas/tmp.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
