# Figma File Summary

## Source

- URL: `https://www.figma.com/design/kOnljWIacjdXFECWRHBiht/Untitled?node-id=0-1&t=tJGvzRxpNbf28qMv-1`
- File key: `kOnljWIacjdXFECWRHBiht`
- Root node: `0:1`
- File/page name at root: `Page 1`

## Scope Of This Read

This summary was produced using only the Figma plugin and Figma skills.

The following Figma plugin reads were completed:

- `get_metadata` on root node `0:1`
- `get_screenshot` on root node `0:1`
- `get_design_context` on `Actividad y Notificaciones` (`1:7`)
- `get_design_context` on `Camara` (`1:1468`)

One additional `get_design_context` request for `Mi Perfil` hit the Figma MCP Starter plan rate limit, so that screen is documented from metadata only.

## High-Level File Description

This file is a mobile app design for `FeedBook`, built around phone-sized frames with a width of `390px`. The visual language is editorial and book-oriented:

- Serif display headings for major titles
- Sans-serif body text for content and UI labels
- Warm off-white backgrounds
- Low-contrast borders and soft shadows
- Muted neutral, slate, brown, and dusty blue accents
- Card-heavy information architecture
- Persistent top app bars and bottom navigation

## Top-Level Canvas Structure

From root metadata, the main top-level nodes on `Page 1` are:

1. `Actividad y Notificaciones` (`1:7`) at `390 x 1132`
2. `Mi Perfil` (`1:125`) at `390 x 1579`
3. `Html → Body` (`1:1325`) at `390 x 1495`
4. `Camara` (`1:1468`) at `390 x 884`
5. Loose asset: `image 26` (`1:1466`)
6. Loose instance: `alarm` (`1:1467`)

## Screen: Actividad y Notificaciones

- Node ID: `1:7`
- Size: `390 x 1132`
- Type: full mobile screen

### Structure

- Header / top app bar
- Main content grouped by time period
- Bottom navigation bar

### Header

- Avatar button on the left
- Center title: `FeedBook`
- Two right-side actions
- Surface styling:
  - warm off-white background
  - subtle bottom border
  - light shadow / blur treatment

### Main Sections

#### Hoy

- Section title: `Hoy`
- Includes two cards:
  - `Article - Card: Like`
  - `Article - Card: Follow/System`

`Article - Card: Like`

- User avatar: `Juan`
- Activity text: `Juan le gustó tu reseña.`
- Preview excerpt:
  - `"Una exploración fascinante sobre el aislamiento y la memoria. La prosa fluye"`
- Timestamp: `HACE 2 HORAS`
- Card style:
  - off-white card
  - bordered
  - rounded corners
  - soft shadow

`Article - Card: Follow/System`

- User label: `Sofía`
- Activity text:
  - `ha comenzado a seguir tu`
  - `biblioteca.`
- Timestamp: `HACE 5 HORAS`
- Left icon sits in a muted blue rounded square

#### Ayer

- Section title: `Ayer`
- Includes one card:
  - `Article - Card: Share/Book`

`Article - Card: Share/Book`

- User avatar: `Elena`
- Activity text: `Elena compartió un nuevo libro.`
- Embedded book snippet:
  - Title: `El Laberinto de los Espíritus`
  - Author: `CARLOS RUIZ ZAFÓN`
- Timestamp: `AYER A LAS 14:30`
- Styling:
  - bordered card
  - inner embedded snippet block
  - dashed border on embedded book area

#### Esta semana

- Section title: `Esta semana`
- Includes one dark recommendation card:
  - `Article - Card: Recommendation`

Recommendation card content:

- Heading: `Recomendación semanal para ti`
- Body:
  - `Basado en tus lecturas recientes de ficción histórica, hemos seleccionado una colección de ensayos que podrían interesarte.`
- CTA: `VER SELECCIÓN`

Visual style:

- Dark charcoal background
- Light text with muted secondary paragraph tone
- Decorative upper-right graphic
- Rounded corners and soft shadow

### Bottom Navigation

- Five nav items:
  - `FEED` active
  - `EXPLORE`
  - `LIBRARY`
  - `STATS`
  - `MESSAGES`
- Active state shown on `FEED`
- Nav bar styling:
  - light background
  - top border
  - soft top shadow

### Typography Notes

- Brand title uses a serif display style
- Section headings use a serif editorial style
- Body copy uses a sans-serif UI font
- Timestamps are small uppercase with tracking

### Visual Notes

- Strong bookish/editorial look
- Spacious vertical rhythm
- Cards and headings dominate layout
- Warm neutral palette with occasional blue and brown accents

## Screen: Mi Perfil

- Node ID: `1:125`
- Size: `390 x 1579`
- Status: documented from metadata only due to MCP rate limit on detailed context

### Top-Level Structure From Metadata

- Header - TopAppBar
- Container
  - Main Content Canvas
    - Profile Identity Section
    - Section - Reading Streak Bento Card
    - Section - Book Lists Bento Grid
- Footer Actions

### Profile Identity Section

Metadata indicates:

- Large centered profile image
- Name heading
- Multi-line bio / description
- Button under profile text

### Reading Streak Section

Metadata indicates a bento-style card with:

- Heading and supporting text
- Numeric streak value
- Day-by-day tactile progress bars:
  - Monday
  - Tuesday
  - Wednesday
  - Thursday
  - Friday
  - Saturday
  - Sunday

Some day bars are marked as read, one as missed, and Sunday appears as the current empty track.

### Book Lists Bento Grid

The metadata shows a multi-block section for book-list presentation, but the root metadata output was truncated before all descendant names could be fully captured.

### Summary

`Mi Perfil` appears to be a profile/dashboard screen centered on:

- profile identity
- reading habit visualization
- personal book list organization

## Screen: Html → Body

- Node ID: `1:1325`
- Size: `390 x 1495`
- Status: documented from root metadata only

### Visible Metadata Signals

This screen appears to be another profile/feed-like mobile layout containing:

- User Header Section
- Interaction Buttons
- Section - Reading Stats Summary
- Section - Currently Reading
- Section - Public Library (Horizontal Scroll)
- Section - Recent Reviews
- Bottom navigation
- Top app bar

### Notable Content Found In Metadata

- User/book content includes:
  - `The Name of the Rose`
  - `Umberto Eco`
  - `One Hundred Years of Solitude`
  - `The Shadow of the Wind`
  - `Ficciones`
  - `Invisible Cities`
- Review section contains a longer review-style text block

### Summary

This looks like a more content-rich public profile or library page, with:

- stats
- current reading progress
- horizontal book shelves
- recent reviews

## Screen: Camara

- Node ID: `1:1468`
- Size: `390 x 884`
- Type: barcode / ISBN scanner screen

### Structure

- Background image and gradient overlays
- Header
- Center scanning instruction
- Barcode frame / scan area
- Footer action

### Header

- Left rounded button: close/back action
- Center title: `Scan ISBN`
- Right rounded utility button

Header style:

- dark transparent overlay
- white title text
- blurred translucent buttons

### Main Scan Area

- Instruction chip:
  - `Align barcode within the frame`
- Large framed scanner area with:
  - four white corner markers
  - inner rectangular border
  - horizontal red scan line with glow

### Footer

- Button text: `Add Manually`
- Light surface on dark background
- Rounded rectangle with subtle depth

### Visual Notes

- Strong camera-overlay feel
- Warm gold light source behind the content
- High contrast between controls and background
- Scanning frame is the primary focal element

## Screenshot Notes

The root screenshot returned a full-canvas image sized:

- delivered size: `1024 x 921`
- original size: `5272 x 4740`

The screenshot visually confirms:

- multiple mobile screens laid out across the canvas
- `Actividad y Notificaciones` on the right side
- `Camara` as a separate dedicated screen
- consistent `FeedBook` visual system across screens

## Inferred Design System Characteristics

Based on the successfully read screens and metadata:

### Layout

- fixed-width mobile frames (`390px`)
- top app bar + content + bottom navigation pattern
- generous section spacing
- repeated use of cards and grouped content blocks

### Components

- top app bar
- rounded icon buttons
- bottom navigation
- activity cards
- recommendation card
- embedded book preview card
- profile header
- reading streak chart
- horizontally scrolling book items
- scanner frame

### Typography

- serif for titles and section headers
- sans-serif for body and UI text
- uppercase micro-labels for timestamps and metadata

### Color Direction

- warm off-white surfaces
- deep slate and charcoal for contrast
- muted gray text hierarchy
- occasional dusty blue accents
- brown/gold used sparingly for emphasis

## Limitations

- Detailed design context for `Mi Perfil` could not be retrieved because the Figma MCP Starter plan rate limit was reached.
- `Html → Body` was not expanded via detailed design context before the rate limit issue.
- Some root metadata was truncated due to response size, so deeper descendants of larger sections are only partially captured here.

## Recommended Next Reads

When the Figma MCP limit is available again, the next useful plugin-only reads would be:

1. `get_design_context` for `Mi Perfil` (`1:125`)
2. `get_design_context` for `Html → Body` (`1:1325`)
3. `get_design_context` for reusable sub-sections inside profile and library screens
4. Optional component inventory extraction for repeated app-bar, card, and nav patterns
