package org.cmas.backend;


import com.google.zxing.WriterException;
import org.cmas.Globals;
import org.cmas.MockUtil;
import org.cmas.entities.cards.CardPrintInfo;
import org.cmas.entities.cards.CardPrintUtil;
import org.cmas.entities.cards.PersonalCard;
import org.cmas.entities.cards.PersonalCardType;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverRegistrationStatus;
import org.cmas.util.barcode.BarcodeEncoder;
import org.cmas.util.barcode.Pixels;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created on Dec 14, 2015
 *
 * @author Alexander Petukhov
 */
public class DrawCardServiceImpl implements DrawCardService {

    private static final float CARD_WIDTH = 640.0f;
    private static final float CARD_HEIGHT = 414.0f;

    private static final float QR_SCALE_FACTOR = 136.0f / CARD_WIDTH;
    private static final float QR_X = 480.0f / CARD_WIDTH;
    private static final float QR_Y = 69.0f / CARD_HEIGHT;

    private static final int CARD_NUMBER_FONT_SIZE = 56;
    private static final float CARD_NUMBER_Y = 210.0f / CARD_HEIGHT;
    private static final float CARD_NUMBER_X_1 = 25.0f / CARD_WIDTH;
    private static final float CARD_NUMBER_X_2 = 200.0f / CARD_WIDTH;
    private static final float CARD_NUMBER_X_3 = 320.0f / CARD_WIDTH;
    private static final float CARD_NUMBER_X_4 = 440.0f / CARD_WIDTH;

    private static final int NAME_FONT_SIZE = 22;
    private static final float NAME_LEFT_X = 200.0f / CARD_WIDTH;
    private static final float LONG_NAME_LEFT_X = 130.0f / CARD_WIDTH;
    private static final float NAME_RIGHT_X = 468.0f / CARD_WIDTH;
    private static final float FIRST_NAME_Y = 90.0f / CARD_HEIGHT;
    private static final float LAST_NAME_Y = 115.0f / CARD_HEIGHT;
    private static final float GUEST_FIRST_NAME_Y = 150.0f / CARD_HEIGHT;
    private static final float GUEST_LAST_NAME_Y = 175.0f / CARD_HEIGHT;
    private static final float DIVER_TYPE_Y_1 = 140.0f / CARD_HEIGHT;
    private static final float DIVER_TYPE_Y_2 = 165.0f / CARD_HEIGHT;

    private static final float STAR_SCALE_FACTOR = 40.0f / CARD_HEIGHT;
    private static final float STAR_Y = 140.0f / CARD_HEIGHT;
    private static final float STAR_X_SPACE = 10.0f / CARD_WIDTH;

    @SuppressWarnings({"OverlyLongMethod", "MagicNumber", "StringConcatenation", "MagicCharacter"})
    @Override
    public BufferedImage drawDiverCard(PersonalCard card, boolean isGold) throws WriterException, IOException {
        String fileName = getFileName(card, isGold);
        BufferedImage initImage = ImageIO.read(DrawCardServiceImpl.class.getResourceAsStream(fileName));
        int width = initImage.getWidth();
        int height = initImage.getHeight();
        BufferedImage finalImage = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = finalImage.createGraphics();
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.drawImage(initImage, 0, 0, null);

        Diver diver = card.getDiver();
        boolean isGuest = diver.getDiverRegistrationStatus() == DiverRegistrationStatus.GUEST;
        String cardNumber = diver.getPrimaryPersonalCard().getNumber();
        String cardNumberToDraw = addLeadingZeors(cardNumber);
        if (card.getCardType() == PersonalCardType.PRIMARY) {
            g2d.setPaint(Color.BLACK);
            g2d.setFont(new Font("Serif", Font.BOLD, NAME_FONT_SIZE));
            g2d.drawString(
                    cardNumberToDraw,
                    CARD_NUMBER_X_1 * (float) width,
                    CARD_NUMBER_Y * (float) height
            );
        }

        if (!isGuest) {
            @SuppressWarnings("NumericCastThatLosesPrecision")
            int qrSize = (int) ((float) width * QR_SCALE_FACTOR);
            Pixels qrCode = BarcodeEncoder.createQRCode(
                    cardNumber, qrSize, qrSize
            );
            BufferedImage qrCodeImage = new BufferedImage(qrCode.width, qrCode.height, BufferedImage.TYPE_INT_RGB);
            qrCodeImage.setRGB(0, 0, qrCode.width, qrCode.height, qrCode.pixels, 0, qrCode.width);
            //noinspection NumericCastThatLosesPrecision
            g2d.drawImage(qrCodeImage, (int) (QR_X * (float) width), (int) (QR_Y * (float) height), null);
        }
        Color nameColor = isGuest ? Color.BLACK : new Color(0x25456c);
        g2d.setPaint(nameColor);
        g2d.setFont(new Font("Serif", Font.BOLD, NAME_FONT_SIZE));
        float leftX = NAME_LEFT_X * (float) width;
        float rightX = NAME_RIGHT_X * (float) width;
        String firstName = diver.getFirstName();
        String lastName = diver.getLastName();
        String fullName = firstName + ' ' + lastName;
        int fullNameWidth = g2d.getFontMetrics().stringWidth(fullName);
        if ((float) fullNameWidth > rightX - leftX) {
            if (isGuest) {
                g2d.drawString(firstName, CARD_NUMBER_X_1 * (float) width, GUEST_FIRST_NAME_Y * (float) height);
                g2d.drawString(lastName, CARD_NUMBER_X_1 * (float) width, GUEST_LAST_NAME_Y * (float) height);
            } else {
                drawWithCenterAlign(g2d, firstName, leftX, rightX, FIRST_NAME_Y * (float) height);
                drawWithCenterAlign(g2d, lastName, leftX, rightX, LAST_NAME_Y * (float) height);
            }
        } else {
            if (isGuest) {
                g2d.drawString(fullName, CARD_NUMBER_X_1 * (float) width, GUEST_FIRST_NAME_Y * (float) height);
            } else {
                drawWithCenterAlign(g2d, fullName, leftX, rightX, FIRST_NAME_Y * (float) height);
            }
        }
        if (!isGuest) {
            CardPrintInfo cardPrintInfo = CardPrintUtil.toPrintName(card);
            int cardPrintNameWidth = g2d.getFontMetrics().stringWidth(cardPrintInfo.printName);
            if ((float) cardPrintNameWidth > rightX - leftX) {
                String[] parts = cardPrintInfo.printName.split(" ");
                float longLeftX = LONG_NAME_LEFT_X * (float) width;
                if (parts.length > 1) {
                    String[] rows = evalOptimalTwoRowsByMinLength(g2d, parts);
                    boolean isLong = false;
                    for (String row : rows) {
                        isLong = (float) g2d.getFontMetrics().stringWidth(row) > rightX - leftX;
                    }
                    if (isLong) {
                        drawWithCenterAlign(g2d, rows[0], longLeftX, rightX, DIVER_TYPE_Y_1 * (float) height);
                        drawWithCenterAlign(g2d, rows[1], longLeftX, rightX, DIVER_TYPE_Y_2 * (float) height);
                    } else {
                        drawWithCenterAlign(g2d, rows[0], leftX, rightX, DIVER_TYPE_Y_1 * (float) height);
                        drawWithCenterAlign(g2d, rows[1], leftX, rightX, DIVER_TYPE_Y_2 * (float) height);
                    }
                } else {
                    drawWithCenterAlign(g2d,
                                        cardPrintInfo.printName,
                                        longLeftX,
                                        rightX,
                                        DIVER_TYPE_Y_1 * (float) height);
                }
            } else {
                drawWithCenterAlign(g2d, cardPrintInfo.printName, leftX, rightX, DIVER_TYPE_Y_1 * (float) height);
            }
            if (cardPrintInfo.drawStars) {
                float fullStarWidth;
                float startPoint;
                float starSize = (float) width * STAR_SCALE_FACTOR;
                float starSpace = STAR_X_SPACE * (float) width;
                float middlePoint = (leftX + rightX) / 2.0f;
                //noinspection NumericCastThatLosesPrecision
                BufferedImage starImage = ImageIO.read(DrawCardServiceImpl.class.getResourceAsStream("star.png"));
                switch (card.getDiverLevel()) {
                    case ONE_STAR:
                        drawStar(starSize, height, g2d, starImage, middlePoint - starSize / 2.0f);
                        break;
                    case TWO_STAR:
                        fullStarWidth = starSize * 2.0f + starSpace;
                        startPoint = middlePoint - fullStarWidth / 2.0f;
                        drawStar(starSize, height, g2d, starImage, startPoint);
                        drawStar(starSize, height, g2d, starImage, startPoint + starSize + starSpace);
                        break;
                    case THREE_STAR:
                        fullStarWidth = starSize * 3.0f + 2.0f * starSpace;
                        startPoint = middlePoint - fullStarWidth / 2.0f;
                        drawStar(starSize, height, g2d, starImage, startPoint);
                        drawStar(starSize, height, g2d, starImage, startPoint + starSize + starSpace);
                        drawStar(starSize, height, g2d, starImage, startPoint + 2.0f * starSize + 2.0f * starSpace);
                        break;
                    case FOUR_STAR:
                        fullStarWidth = starSize * 4.0f + 2.0f * starSpace;
                        startPoint = middlePoint - fullStarWidth / 2.0f;
                        drawStar(starSize, height, g2d, starImage, startPoint);
                        drawStar(starSize, height, g2d, starImage, startPoint + starSize + starSpace);
                        drawStar(starSize, height, g2d, starImage, startPoint + 2.0f * starSize + 2.0f * starSpace);
                        drawStar(starSize, height, g2d, starImage, startPoint + 3.0f * starSize + 3.0f * starSpace);
                        break;
                }
            }
        }
        g2d.dispose();
        return finalImage;
    }

    private static String addLeadingZeors(String cardNumber) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Globals.SPORTS_CARD_NUMBER_MAX_LENGTH - cardNumber.length(); i++) {
            sb.append('0');
        }
        sb.append(cardNumber);
        return sb.toString();
    }

    @Override
    public String getFileName(PersonalCard card, boolean isGold) {
        DiverRegistrationStatus diverRegistrationStatus = card.getDiver().getDiverRegistrationStatus();
        if (diverRegistrationStatus == DiverRegistrationStatus.GUEST) {
            return isGold ? "aqualink_gold.png" : "aqualink_silver.png";
        }
        String fileName = "cmas_card.png";
        switch (card.getCardType()) {
            case CHILDREN_DIVING:
            case PRIMARY:
            case NATIONAL:
                fileName = "cmas_card.png";
                break;
            case EXTENDED_RANGE:
            case TRIMIX:
            case TRIMIX_GASBLENDER:
            case OXYGEN_ADMINISTATOR:
            case REBREATHER:
            case NITROX:
            case NITROX_GASBLENDER:
                fileName = "cmas_card_yellow.png";
                break;
            case APNOEA:
                fileName = "cmas_card_apnoea.png";
                break;
            case DRY_SUIT:
            case ICE_DIVING:
            case SIDE_MOUNT:
            case CAVE:
            case ALTITUDE_DIVER:
            case COMPRESSOR_OPERATOR:
            case DISABLED_DIVING:
            case DRIFT_DIVING:
            case GYMSWIMMING:
            case HYDROBIKE:
            case INTRO_TO_SCUBA:
            case NAVIGATION:
            case NIGHT:
            case PHOTO:
            case RESCUE:
            case SCOOTER:
            case SELF_RESCUE:
            case SKILLS:
            case SNORKEL:
            case WRECK:
                fileName = "cmas_card_gray.png";
                break;
            case SCIENTIFIC:
            case UNDERWATER_ARCHAEOLOGY:
            case UNDERWATER_GEOLOGY:
            case FRESHWATER_BIOLOGY:
            case OCEAN_DISCOVERY:
            case MARINE_BIOLOGY:
            case HERITAGE_DISCOVERY:
                fileName = "cmas_card_green.png";
                break;
        }
        return fileName;
    }

    @SuppressWarnings("NumericCastThatLosesPrecision")
    private static void drawStar(float starSize, int height, Graphics2D g2d, BufferedImage starImage, float x) {
        BufferedImage after = ImageResizer.scaleDown(starImage, starSize, starSize);
        g2d.drawImage(after, (int) x, (int) (STAR_Y * (float) height), null);
    }

    private static void drawWithCenterAlign(Graphics2D g2d, String text, float leftX, float rightX, float y) {
        int actualWidth = g2d.getFontMetrics().stringWidth(text);
        @SuppressWarnings("MagicNumber")
        float finalX = (leftX + rightX) / 2.0f - (float) actualWidth / 2.0f;
        g2d.drawString(text, finalX, y);
    }

    @SuppressWarnings({"MagicCharacter", "StringConcatenation"})
    private static String[] evalOptimalTwoRowsByMinLength(Graphics2D g2d, String[] parts) {
        if (parts.length < 2) {
            return parts;
        }
        String[] result = {"", ""};
        if (parts.length % 2 == 0) {
            for (int i = 0; i < parts.length / 2 - 1; i++) {
                result[0] += parts[i] + ' ';
            }
            result[0] += parts[parts.length / 2 - 1];

            for (int i = parts.length / 2; i < parts.length - 1; i++) {
                result[1] += parts[i] + ' ';
            }
            result[1] += parts[parts.length - 1];
        } else {
            StringBuilder option1 = new StringBuilder();
            for (int i = 0; i < parts.length / 2; i++) {
                option1.append(parts[i]).append(' ');
            }
            option1.append(parts[parts.length / 2]);

            StringBuilder option2 = new StringBuilder();
            for (int i = parts.length / 2; i < parts.length - 1; i++) {
                option2.append(parts[i]).append(' ');
            }
            option2.append(parts[parts.length - 1]);

            if (g2d.getFontMetrics().stringWidth(option1.toString()) <
                g2d.getFontMetrics().stringWidth(option2.toString())) {
                result[0] = option1.toString();
                for (int i = parts.length / 2 + 1; i < parts.length - 1; i++) {
                    result[1] += parts[i] + ' ';
                }
                result[1] += parts[parts.length - 1];
            } else {
                for (int i = 0; i < parts.length / 2 - 1; i++) {
                    result[0] += parts[i] + ' ';
                }
                result[0] += parts[parts.length / 2 - 1];
                result[1] = option2.toString();
            }
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            BufferedImage image = new DrawCardServiceImpl().drawDiverCard(MockUtil.getDiver().getPrimaryPersonalCard(),
                                                                          true);
            ImageIO.write(image, "png", new File("/Users/sunsunich/workplace/сmas/tmp.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
