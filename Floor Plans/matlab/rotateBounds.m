function newBounds = rotateBounds(BW, bounds)
    [~,h] = size(BW);
    newBounds = zeros(size(bounds));
    for i=1:size(bounds,1)
        newBounds(i,:) = [bounds(i,2),h-bounds(i,1)-bounds(i,4),bounds(i,4),bounds(i,3)];
    end
end