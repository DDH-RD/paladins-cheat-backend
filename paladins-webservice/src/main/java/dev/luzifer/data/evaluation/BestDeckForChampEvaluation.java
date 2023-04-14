package dev.luzifer.data.evaluation;

import dev.luzifer.data.access.GameDao;
import dev.luzifer.data.match.info.ChampData;
import dev.luzifer.spring.controller.GameController;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class BestDeckForChampEvaluation implements Evaluation<Map<Integer, Integer>> {

    private final int champId;
    private final GameDao gameDao;
    private final Double season;

    @Override
    public Map<Integer, Integer> evaluate() {

        Map<ChampData, CardMeter[]> cardsForGame = preparation();
        Map<CardMeter, Double> averagePointsMap = calculateAveragePointsForEachCard(cardsForGame.values());
        Map<CardMeter, Integer> weightedPointsMap = weightCardsByMatchOutcome(cardsForGame);

        return calculateThePerfectDeck(weightedPointsMap, averagePointsMap);
    }

    @Override
    public Map<Integer, Integer> evaluate(int champCategory) {
        throw new UnsupportedOperationException("This evaluation does not support this method");
    }

    private Map<CardMeter, Integer> weightCardsByMatchOutcome(Map<ChampData, CardMeter[]> cardsForGame) {
        Map<CardMeter, Integer> cardPoints = new HashMap<>();
        for (Map.Entry<ChampData, CardMeter[]> entry : cardsForGame.entrySet()) {

            ChampData champData = entry.getKey();
            CardMeter[] champCards = entry.getValue();

            for (CardMeter cardMeter : champCards) {
                int points = champData.getWon() == 0 ?
                        Math.min(champData.getTeam1Points(), champData.getTeam2Points()) :
                        Math.max(champData.getTeam1Points(), champData.getTeam2Points());

                if (cardPoints.containsKey(cardMeter))
                    cardPoints.put(cardMeter, cardPoints.get(cardMeter) + points);
                else
                    cardPoints.put(cardMeter, points);
            }
        }
        return cardPoints;
    }

    private Map<Integer, Integer> calculateThePerfectDeck(Map<CardMeter, Integer> cardsWeightMap, Map<CardMeter, Double> averagePointsMap) {

        Map<Integer, Integer> perfectDeck = new HashMap<>();

        double remainingPoints = 15.0;
        while (!cardsWeightMap.isEmpty() && remainingPoints > 0) {

            CardMeter bestCard = null;
            double bestScore = Double.NEGATIVE_INFINITY;

            for (Map.Entry<CardMeter, Double> entry : averagePointsMap.entrySet()) {

                CardMeter card = entry.getKey();
                double averagePoints = entry.getValue();

                if (cardsWeightMap.containsKey(card)) {
                    double score = (cardsWeightMap.get(card) * averagePoints) / card.getPoints();
                    if (score > bestScore) {
                        bestCard = card;
                        bestScore = score;
                    }
                }
            }

            if (bestCard != null) {
                int pointsToAdd = Math.min(bestCard.getPoints(), (int) Math.floor(remainingPoints));
                perfectDeck.put(bestCard.getCardId(), pointsToAdd);

                remainingPoints -= pointsToAdd;
                cardsWeightMap.remove(bestCard);
            } else {
                break;
            }
        }

        return perfectDeck;
    }

    private Map<CardMeter, Double> calculateAveragePointsForEachCard(Collection<CardMeter[]> cards) {

        Map<CardMeter, Double> averagePointsMap = new HashMap<>();
        Map<CardMeter, Integer> totalPointsMap = new HashMap<>();

        for (CardMeter[] card : cards) {
            for(CardMeter cardMeter : card) {
                if (totalPointsMap.containsKey(cardMeter)) {
                    totalPointsMap.put(cardMeter, totalPointsMap.get(cardMeter) + cardMeter.getPoints());
                } else {
                    totalPointsMap.put(cardMeter, cardMeter.getPoints());
                }
            }
        }

        for (Map.Entry<CardMeter, Integer> entry : totalPointsMap.entrySet()) {
            CardMeter card = entry.getKey();
            int totalPoints = entry.getValue();

            averagePointsMap.put(card, (double) totalPoints / cards.size());
        }

        return averagePointsMap;
    }

    private Map<ChampData, CardMeter[]> preparation() {

        List<ChampData> champDataList = gameDao.fetchChampDataForChamp(season, champId);

        Map<ChampData, CardMeter[]> cardMap = new HashMap<>();
        for(ChampData champData : champDataList) {
            CardMeter card1 = new CardMeter(champData.getDeckCard1(), champData.getDeckCard1Level());
            CardMeter card2 = new CardMeter(champData.getDeckCard2(), champData.getDeckCard2Level());
            CardMeter card3 = new CardMeter(champData.getDeckCard3(), champData.getDeckCard3Level());
            CardMeter card4 = new CardMeter(champData.getDeckCard4(), champData.getDeckCard4Level());
            CardMeter card5 = new CardMeter(champData.getDeckCard5(), champData.getDeckCard5Level());
            CardMeter[] cards = new CardMeter[] {card1, card2, card3, card4, card5};
            cardMap.put(champData, cards);
        }

        return cardMap;
    }

    @Value
    @Getter
    @EqualsAndHashCode
    public static class CardMeter {

        int cardId;
        int points;
    }
}
