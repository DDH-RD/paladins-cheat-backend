package dev.luzifer.data.evaluation;

import dev.luzifer.data.access.GameDao;
import dev.luzifer.data.match.info.ChampDto;
import dev.luzifer.data.match.info.GameDto;
import dev.luzifer.spring.controller.GameController;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * In Paladins you can have 5 cards per deck with a total of 15 given points. Each card can only have up to 5 points.
 * This evaluation will calculate the best deck for a given champion.
 */
@RequiredArgsConstructor
public class BestDeckForChampEvaluation implements Evaluation<Map<BestDeckForChampEvaluation.CardMeter, Integer>> {

    private final int champId;
    private final GameDao gameDao;
    private final GameController.MatchType matchType;

    @Override
    public Map<CardMeter, Integer> evaluate() {

        Map<GameDto, Map<ChampDto, CardMeter[]>> cardsForGame = preparation();
        Map<CardMeter, Double> averagePointsMap = calculateAveragePointsForEachCard(cardsForGame.values().iterator().next().values().iterator().next());
        Map<CardMeter, Integer> weightedPointsMap = weightCardsByMatchOutcome(cardsForGame);

        return calculateThePerfectDeck(weightedPointsMap, averagePointsMap);
    }

    @Override
    public Map<CardMeter, Integer> evaluate(int champCategory) {
        throw new UnsupportedOperationException("This evaluation does not support this method");
    }

    private Map<CardMeter, Integer> weightCardsByMatchOutcome(Map<GameDto, Map<ChampDto, CardMeter[]>> cardsForGame) {
        Map<CardMeter, Integer> cardPoints = new HashMap<>();
        for (Map.Entry<GameDto, Map<ChampDto, CardMeter[]>> entry : cardsForGame.entrySet()) {
            GameDto game = entry.getKey();
            Map<ChampDto, CardMeter[]> champCards = entry.getValue();
            for (Map.Entry<ChampDto, CardMeter[]> champCardEntry : champCards.entrySet()) {
                ChampDto champ = champCardEntry.getKey();
                CardMeter[] cards = champCardEntry.getValue();
                for (CardMeter card : cards) {
                    int points = champ.getWon() == 0 ? Math.min(game.getTeam1Points(), game.getTeam2Points()) : Math.max(game.getTeam1Points(), game.getTeam2Points());
                    if (cardPoints.containsKey(card)) {
                        cardPoints.put(card, cardPoints.get(card) + points);
                    } else {
                        cardPoints.put(card, points);
                    }
                }
            }
        }
        return cardPoints;
    }

    private Map<CardMeter, Integer> calculateThePerfectDeck(Map<CardMeter, Integer> cardsWeightMap, Map<CardMeter, Double> averagePointsMap) {
        Map<CardMeter, Integer> perfectDeck = new HashMap<>();
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
                perfectDeck.put(bestCard, pointsToAdd);
                remainingPoints -= pointsToAdd;
                cardsWeightMap.remove(bestCard);
            } else {
                break;
            }
        }

        return perfectDeck;
    }

    private Map<CardMeter, Double> calculateAveragePointsForEachCard(CardMeter[] cards) {
        Map<CardMeter, Double> averagePointsMap = new HashMap<>();

        Map<CardMeter, Integer> totalPointsMap = new HashMap<>();
        for (CardMeter card : cards) {
            if (totalPointsMap.containsKey(card)) {
                totalPointsMap.put(card, totalPointsMap.get(card) + card.getPoints());
            } else {
                totalPointsMap.put(card, card.getPoints());
            }
        }

        for (Map.Entry<CardMeter, Integer> entry : totalPointsMap.entrySet()) {
            CardMeter card = entry.getKey();
            int totalPoints = entry.getValue();
            double averagePoints = ((double) totalPoints) / cards.length;
            averagePointsMap.put(card, averagePoints);
        }

        return averagePointsMap;
    }

    private Map<GameDto, Map<ChampDto, CardMeter[]>> preparation() {
        GameDto[] games = gameDao.fetchMatchesWithChamp(matchType, champId);
        Map<GameDto, Map<ChampDto, CardMeter[]>> resultMap = new HashMap<>();
        for(GameDto game : games) {
            for(ChampDto champ : game.getChamps()) {
                if(champ.getId() == champId) {
                    Map<ChampDto, CardMeter[]> cardMap = new HashMap<>();
                    CardMeter card1 = new CardMeter(champ.getDeckCard1(), champ.getDeckCard1Level());
                    CardMeter card2 = new CardMeter(champ.getDeckCard2(), champ.getDeckCard2Level());
                    CardMeter card3 = new CardMeter(champ.getDeckCard3(), champ.getDeckCard3Level());
                    CardMeter card4 = new CardMeter(champ.getDeckCard4(), champ.getDeckCard4Level());
                    CardMeter card5 = new CardMeter(champ.getDeckCard5(), champ.getDeckCard5Level());
                    CardMeter[] cards = new CardMeter[] {card1, card2, card3, card4, card5};
                    cardMap.put(champ, cards);
                    resultMap.put(game, cardMap);
                }
            }
        }
        return resultMap;
    }

    @Value
    @Getter
    @EqualsAndHashCode
    public static class CardMeter {

        int cardId;
        int points;
    }
}
