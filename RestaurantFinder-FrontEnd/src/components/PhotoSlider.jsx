import React, { useState } from "react";

const PhotoSlider = ({ photos }) => {
  const [currentIndex, setCurrentIndex] = useState(0); // State to track the current photo

  if (!photos || photos.length === 0) {
    return <p className="text-gray-500">No photos available.</p>;
  }

  const handlePrev = () => {
    setCurrentIndex((prevIndex) =>
      prevIndex === 0 ? photos.length - 1 : prevIndex - 1
    );
  };

  const handleNext = () => {
    setCurrentIndex((prevIndex) =>
      prevIndex === photos.length - 1 ? 0 : prevIndex + 1
    );
  };

  return (
    <div className="relative w-full h-48 overflow-hidden">
      <img
        src={photos[currentIndex]}
        alt={`${currentIndex + 1}`}
        className="w-full h-48 object-cover"
      />
      {/* Navigation Buttons */}
      <button
        onClick={handlePrev}
        className="absolute top-1/2 left-2 transform -translate-y-1/2 bg-gray-800 text-white rounded-full px-2 py-1"
      >
        {"<"}
      </button>
      <button
        onClick={handleNext}
        className="absolute top-1/2 right-2 transform -translate-y-1/2 bg-gray-800 text-white rounded-full px-2 py-1"
      >
        {">"}
      </button>
      {/* Indicator Dots */}
      <div className="absolute bottom-2 left-1/2 transform -translate-x-1/2 flex gap-1">
        {photos.map((_, index) => (
          <span
            key={index}
            className={`w-2 h-2 rounded-full ${
              index === currentIndex ? "bg-blue-500" : "bg-gray-400"
            }`}
          />
        ))}
      </div>
    </div>
  );
};

export default PhotoSlider;