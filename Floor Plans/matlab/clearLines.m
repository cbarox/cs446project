function BW2 = clearLines(BW, extent, length)
    BW2 = BW;
    [x,y] = findPoint(BW2, extent, length, extent+1);
    while x > 0
        BW2 = imfill(BW2, [x,y]);
        [x,y] = findPoint(BW2, extent, length, x);
    end
    
    % Todo is there also a way to remove long lines, not just thick areas?
end

function [x,y] = findPoint(BW, extent, length, x)
    [w,h] = size(BW);
    for i=x:w-extent
        for j=extent+1:h-extent
            if checkPoint(BW, extent, i, j) || checkLine(BW, length, i, j)
                x = i;
                y = j;
                return;
            end
        end
    end
    x = -1;
    y = -1;
end

function found = checkPoint(BW, extent, x, y)
    for i=-extent:extent
        for j = -extent:extent
            if BW(x+i,y+j)
                found = 0;
                return;
            end
        end
    end
    found = 1;
end

function found = checkLine(BW, length, x, y)
    [w,h] = size(BW);
    foundI = 1;
    foundJ = 1;
    if w - x < length
        foundI = 0;
    else
        for i=0:length
            if BW(x+i,y)
                foundI = 0;
                break;
            end
        end
    end
    if h - y < length
        foundJ = 0;
    else
        for j=0:length
            if BW(x,y+j)
                foundJ = 0;
                break;
            end
        end
    end
    found = foundI || foundJ;
end