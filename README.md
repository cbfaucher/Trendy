# Trendy

# What is it?

A homegrown tool to simulate various stock quote strategies.

# How is it?

Being built - still a work-in-progress.  But being built. ;-)

# Strategies

| Strategy     |                            | Buy Conditions                                                                                   | Sell Conditions                                                                                                                      | Status |
|--------------|----------------------------|--------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------|--------|
| Lambert      | See François Lambert       | (RSI <= 30 *AND* CRSI <= [20,Lower band]) <br>*AND* <br>Candle around/above EMA (short or long) | RSI>=70 *OR* (CRSI >= [80,High Band] <br>_THEN_ <br>RSI goes below 70 <br>*OR* cRSI goes below [80\|High Band] <br>*OR* CLOSE < EMA | 80%    |
| EMA Crossing | Short EMA crosses Long EMA | Short EMA crosses up Long EMA                                                                    | Short EMA crosses down Long EMA                                                                                                      | DONE   |
|              |                            |                                                                                                  |                                                                                                                                      |        |