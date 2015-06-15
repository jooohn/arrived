export default function EventFactory($resource) {
  return $resource('/api/areas/:areaId/events/:id', {
    id: '@id',
    areaId: '@areaId'
  });
}
