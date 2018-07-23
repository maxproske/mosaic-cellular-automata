# Mosaic Cellular Automata
Given any image as input, our method creates an initial grid of alive and dead pixels, and runs the result as a “Game of Life” simulation, in which cells are killed or born depending on the state of their neighbours. Our pattern sustains itself indefinitely, maintaining the integrity of the image.

For more information, please download our [presentation slides](https://github.com/maxproske/mosaic-cellular-automata/raw/master/public/MCA_Presentation_Slides.pdf) or [research paper](https://github.com/maxproske/mosaic-cellular-automata/raw/master/public/MCA_Paper.pdf).

![Mattes](https://github.com/maxproske/mosaic-cellular-automata/raw/master/public/mattes.png)

![Closeup](https://github.com/maxproske/mosaic-cellular-automata/raw/master/public/closeup.png)

## Challenges
- Combining oscillators into complex life forms that move around the image.
- Optimizing 2^22 (4.19 million) cells to animate once per second.
- Debugging when one misplaced pixel would rip the image apart.
- Massive overhead for research and planning program structure. Compiling a list of 250+ oscillators by size, period, heat, and volatility, which make for long, interesting lifespans.

## Credit
**Max Proske** - Matte and filter development, oscillator propagation, research

**Sam Springer** - Program structure, GUI and viewport, performance optimization 