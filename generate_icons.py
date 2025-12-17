#!/usr/bin/env python3
"""
Simple script to generate basic PNG icons for Bloomify app
Requires: pip install Pillow
"""

from PIL import Image, ImageDraw, ImageFont
import os

def create_circular_icon(size, bg_color, text, text_color="white", filename="icon.png"):
    """Create a circular icon with text"""
    # Create image with transparent background
    img = Image.new('RGBA', (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    # Draw circle
    draw.ellipse([0, 0, size-1, size-1], fill=bg_color)
    
    # Add text
    try:
        font_size = size // 2
        font = ImageFont.truetype("arial.ttf", font_size)
    except:
        font = ImageFont.load_default()
    
    # Get text size and center it
    bbox = draw.textbbox((0, 0), text, font=font)
    text_width = bbox[2] - bbox[0]
    text_height = bbox[3] - bbox[1]
    
    x = (size - text_width) // 2
    y = (size - text_height) // 2
    
    draw.text((x, y), text, fill=text_color, font=font)
    
    # Save image
    img.save(filename, "PNG")
    print(f"Created: {filename}")

def create_app_logo(size=120):
    """Create the main app logo"""
    img = Image.new('RGBA', (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    # Create gradient-like effect with multiple circles
    colors = [
        (76, 175, 80),    # Green
        (255, 64, 129),   # Pink
        (3, 218, 198),    # Teal
        (255, 213, 79),   # Yellow
        (156, 39, 176)    # Purple
    ]
    
    center = size // 2
    petal_size = size // 4
    
    # Draw petals around center
    positions = [
        (center, center - petal_size),  # Top
        (center + petal_size, center),  # Right
        (center, center + petal_size),  # Bottom
        (center - petal_size, center),  # Left
        (center - petal_size//2, center - petal_size//2),  # Top-left
    ]
    
    for i, (x, y) in enumerate(positions):
        color = colors[i % len(colors)]
        draw.ellipse([x-petal_size//2, y-petal_size//2, 
                     x+petal_size//2, y+petal_size//2], fill=color)
    
    # Draw center circle
    center_size = petal_size
    draw.ellipse([center-center_size//2, center-center_size//2,
                 center+center_size//2, center+center_size//2], 
                fill=(76, 175, 80))  # Green center
    
    img.save("ic_bloomify_logo.png", "PNG")
    print("Created: ic_bloomify_logo.png")

def main():
    """Generate all required icons"""
    print("Generating Bloomify app icons...")
    
    # Create directories
    os.makedirs("drawable", exist_ok=True)
    
    # App logo
    create_app_logo()
    
    # Navigation icons
    create_circular_icon(48, (76, 175, 80), "✓", filename="ic_habit.png")      # Green
    create_circular_icon(48, (255, 64, 129), "😊", filename="ic_mood.png")     # Pink
    create_circular_icon(48, (33, 150, 243), "💧", filename="ic_water.png")    # Blue
    create_circular_icon(48, (117, 117, 117), "⚙", filename="ic_settings.png") # Gray
    
    # Action icons
    create_circular_icon(48, (255, 64, 129), "+", filename="ic_add.png")       # Pink
    create_circular_icon(48, (255, 152, 0), "✏", filename="ic_edit.png")      # Orange
    create_circular_icon(48, (244, 67, 54), "🗑", filename="ic_delete.png")    # Red
    
    # Mood emoji backgrounds (larger for better visibility)
    moods = [
        ("😊", (255, 213, 79), "mood_happy.png"),    # Yellow
        ("😢", (66, 165, 245), "mood_sad.png"),      # Blue
        ("😠", (239, 83, 80), "mood_angry.png"),     # Red
        ("🤩", (255, 112, 67), "mood_excited.png"),  # Orange
        ("😌", (102, 187, 106), "mood_calm.png"),    # Green
        ("😐", (189, 189, 189), "mood_neutral.png")  # Gray
    ]
    
    for emoji, color, filename in moods:
        create_circular_icon(60, color, emoji, filename=filename)
    
    # Water glass icon
    create_circular_icon(48, (33, 150, 243), "🥤", filename="ic_water_glass.png")
    
    print("\n✅ All icons generated successfully!")
    print("📁 Files saved in current directory")
    print("📱 Copy these PNG files to your app/src/main/res/drawable/ folder")

if __name__ == "__main__":
    main()

