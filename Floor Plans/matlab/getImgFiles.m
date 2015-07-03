function fileList = getImgFiles(dirName)

  dirData = dir(dirName);      %# Get the data for the current directory
  dirIndex = [dirData.isdir];  %# Find the index for directories
  fileList = {dirData(~dirIndex).name}';  %'# Get a list of the files
  if ~isempty(fileList)
    %fileList = cellfun(@(x) fullfile(dirName,x),...  %# Prepend path to files
    %                   fileList,'UniformOutput',false);
                   
    % Filter out all non-images
    fileListPNG = fileList(cellfun(@(x) size(x,1)>0, strfind(fileList, '.png')));
    fileListJSON = fileList(cellfun(@(x) size(x,1)>0, strfind(fileList, '.json')));
    fileListJSONOnlyName = cellfun(@(x) x(1:end-5), fileListJSON, 'UniformOutput', false);
    fileList = fileListPNG(cellfun(@(x) size(find(ismember(fileListJSONOnlyName, x(1:end-4))), 1)==0, fileListPNG));
  end
end