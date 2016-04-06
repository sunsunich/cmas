package org.cmas.backend;


/**
 * Created on Dec 14, 2015
 *
 * @author Alexander Petukhov
 */
public class DrawCardService {

    private static final float QR_SCALE_FACTOR = 136.0f / 640.0f;
    private static final float QR_X = 480.0f / 640.0f;
    private static final float QR_Y = 69.0f / 414.0f;

    private static final float CARD_NUMBER_SCALE_FACTOR = 50.0f / 414.0f;
    private static final float CARD_NUMBER_Y = 350.0f / 414.0f;
    private static final float CARD_NUMBER_X_1 = 80.0f / 640.0f;
    private static final float CARD_NUMBER_X_2 = 200.0f / 640.0f;
    private static final float CARD_NUMBER_X_3 = 320.0f / 640.0f;
    private static final float CARD_NUMBER_X_4 = 440.0f / 640.0f;
    private static final float CARD_NUMBER_GRAD_START = 100.0f / 640.0f;
    private static final float CARD_NUMBER_GRAD_END = 560.0f / 640.0f;

    private static final float NAME_SCALE_FACTOR = 20.0f / 414.0f;
    private static final float NAME_X = 468.0f / 640.0f;
    private static final float FIRST_NAME_Y = 90.0f / 414.0f;
    private static final float LAST_NAME_Y = 115.0f / 414.0f;
    private static final float DIVER_TYPE_Y = 140.0f / 414.0f;
//    private static final float NAME_GRAD_START = 25.0f / 640.0f;
//    private static final float NAME_GRAD_END = 425.0f / 640.0f;

    private static final float STAR_SCALE_FACTOR = 40.0f / 414.0f;
    private static final float STAR_Y = 140.0f / 414.0f;
    private static final float STAR_X_1 = 280.0f / 640.0f;
    private static final float STAR_X_2 = 340.0f / 640.0f;
    private static final float STAR_X_3 = 400.0f / 640.0f;

/*
    @SuppressWarnings("OverlyBroadThrowsClause")
    public static Drawable drawDiverPrimaryCard(Diver diver) throws Exception {
        Resources resources = context.getResources();
        Drawable backgroundImage = resources.getDrawable(R.drawable.cmas_card);
        //        getDrawableForDensity(
//                R.drawable.cmas_card, context.getResources().getDisplayMetrics().densityDpi, context.getTheme()
//        );
        int width = backgroundImage.getMinimumWidth();
        int height = backgroundImage.getMinimumHeight();


        Bitmap canvasBitmap = Bitmap.createBitmap(
                width, height,
                Bitmap.Config.ARGB_8888);
        // Create a canvas, that will draw on to canvasBitmap.
        Canvas canvas = new Canvas(canvasBitmap);
        backgroundImage.draw(canvas);

        Paint cardPaint = new Paint();
        cardPaint.setTextSize(CARD_NUMBER_SCALE_FACTOR * (float) height);
        cardPaint.setFakeBoldText(true);
        cardPaint.setShader(
                new LinearGradient(
                        CARD_NUMBER_GRAD_START, 0, CARD_NUMBER_GRAD_END * (float) width, 0,
                        resources.getColor(R.color.cardNumberGradStartColor),
                        resources.getColor(R.color.cardNumberGradEndColor),
                        Shader.TileMode.REPEAT
                )
        );

        String cardNumber = diver.getPrimaryPersonalCard().getNumber();
        String cardNumber1 = cardNumber.substring(0, 4);
        canvas.drawText(
                cardNumber1,
                CARD_NUMBER_X_1 * (float) width,
                CARD_NUMBER_Y * (float) height,
                cardPaint
        );
        String cardNumber2 = cardNumber.substring(4, 8);
        canvas.drawText(
                cardNumber2,
                CARD_NUMBER_X_2 * (float) width,
                CARD_NUMBER_Y * (float) height,
                cardPaint
        );
        String cardNumber3 = cardNumber.substring(8, 12);
        canvas.drawText(
                cardNumber3,
                CARD_NUMBER_X_3 * (float) width,
                CARD_NUMBER_Y * (float) height,
                cardPaint
        );
        String cardNumber4 = cardNumber.substring(12);
        canvas.drawText(
                cardNumber4,
                CARD_NUMBER_X_4 * (float) width,
                CARD_NUMBER_Y * (float) height,
                cardPaint
        );

        Paint namePaint = new Paint();
        namePaint.setTextSize(NAME_SCALE_FACTOR * (float) height);
        namePaint.setFakeBoldText(true);
        namePaint.setTextAlign(Paint.Align.RIGHT);
        namePaint.setColor(resources.getColor(R.color.cardNameColor));
//        namePaint.setShader(
//                new LinearGradient(
//                        NAME_GRAD_START, 0, NAME_GRAD_END * (float) width, 0,
//                        resources.getColor(R.color.cardNameGradStarColor),
//                        resources.getColor(R.color.cardNameGradEndColor),
//                        Shader.TileMode.CLAMP
//                )
//        );
        canvas.drawText(
                diver.getFirstName(),
                NAME_X * (float) width,
                FIRST_NAME_Y * (float) height,
                namePaint
        );
        canvas.drawText(
                diver.getLastName(),
                NAME_X * (float) width,
                LAST_NAME_Y * (float) height,
                namePaint
        );
        String diverTypeStr = "";
        switch (diver.getDiverType()) {
            case DIVER:
                diverTypeStr = "DIVER";
                break;
            case INSTRUCTOR:
                diverTypeStr = "NITROX INSTRUCTOR";
                break;
        }
        canvas.drawText(
                diverTypeStr,
                NAME_X * (float) width,
                DIVER_TYPE_Y * (float) height,
                namePaint
        );
        @SuppressWarnings("NumericCastThatLosesPrecision")
        int qrSize = (int) ((float) width * QR_SCALE_FACTOR);
        Bitmap qrCode = BarcodeEncoder.createQRCode(
                cardNumber, qrSize, qrSize
        );
        canvas.drawBitmap(qrCode, QR_X * (float) width, QR_Y * (float) height, null);

        if (diver.getDiverLevel() != null) {
            float starSize = (float) width * STAR_SCALE_FACTOR;
            Drawable starImage = resources.getDrawable(R.drawable.star);
            switch (diver.getDiverLevel()) {
                case THREE_STAR:
                    //noinspection NumericCastThatLosesPrecision
                    starImage.setBounds(
                            (int) (STAR_X_3 * (float) width),
                            (int) (STAR_Y * (float) height),
                            (int) (STAR_X_3 * (float) width + starSize),
                            (int) (STAR_Y * (float) height + starSize)
                    );
                    starImage.draw(canvas);
                    //fall through
                case TWO_STAR:
                    //noinspection NumericCastThatLosesPrecision
                    starImage.setBounds(
                            (int) (STAR_X_2 * (float) width),
                            (int) (STAR_Y * (float) height),
                            (int) (STAR_X_2 * (float) width + starSize),
                            (int) (STAR_Y * (float) height + starSize)
                    );
                    starImage.draw(canvas);
                    //fall through
                case ONE_STAR:
                    //noinspection NumericCastThatLosesPrecision
                    starImage.setBounds(
                            (int) (STAR_X_1 * (float) width),
                            (int) (STAR_Y * (float) height),
                            (int) (STAR_X_1 * (float) width + starSize),
                            (int) (STAR_Y * (float) height + starSize)
                    );
                    starImage.draw(canvas);
                    break;
            }
        }

        // Combine background and text to a LayerDrawable
        return new LayerDrawable(
                new Drawable[]{backgroundImage, new BitmapDrawable(canvasBitmap)});
    }
    */
}
