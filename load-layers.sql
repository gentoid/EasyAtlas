delete from roads;
delete from buildings;
delete from addr_points;
delete from rivers_ways;
delete from water_polygons;
delete from railroads;
delete from amenity_polygons;
delete from poi;
delete from coastline;
delete from barier_line;
delete from barier_point;
delete from landuse;
delete from leisure;



insert into roads
select
  nextval('roads_id_seq'),  
  tags -> 'highway',
  tags -> 'name',
  tags -> 'ref',
  tags -> 'oneway' in ('yes', '1', 'true'),
  tags -> 'bridge' in ('yes', '1', 'true'),
  tags -> 'tunnel' in ('yes', '1', 'true'),
  tags -> 'surface',
  tags -> 'smoothenes',
  NULLIF(substring(tags -> 'maxspeed' from '^\\d+$'), '')::smallint,
  NULLIF(substring(tags -> 'lanes' from '^\\d+$'), '')::smallint,
  linestring
from
  ways
where
 tags ? 'highway' 
 and tags -> 'highway' 
	in ('motorway', 'trunk', 'primary', 'secondary', 'tertiary', 'residential', 'unclassified', 'service', 'track', 'pedestrian', 'footway', 'path', 'steps')
 or tags -> 'highway' like '%link'
 and not tags @> hstore(ARRAY['area', 'yes']);


insert into buildings 
select
  nextval('buildings_id_seq'),
  tags->'building',
  tags->'name',
  tags->'addr:housenumber',
  tags->'addr:street',
  NULLIF(substring(tags -> 'building:levels' from '^\\d+$'), '')::smallint,  
  ST_GeometryN(geom, 1)
from polygons
  where tags ? 'building' and not tags -> 'building' in ('no', '0', 'false');


insert into addr_points 
select
  tags->'addr:housenumber',
  tags->'addr:street',
  null,
  true,
  ST_PointOnSurface(ST_GeometryN(geom, 1))
from polygons
  where tags ? 'building' and tags ? 'addr:housenumber';

insert into addr_points 
select
  tags->'addr:housenumber',
  tags->'addr:street',
  null,
  false,
  geom
from nodes 
where tags ? 'addr:housenumber';

update addr_points p
set angle=ST_Azimuth(ST_StartPoint(st_shortestline(p.geom, r.geom)), ST_EndPoint(st_shortestline(p.geom, r.geom))) * 57.2957795
from roads r
where r.name=p.street;

insert into rivers_ways
select
  tags->'waterway',
  tags->'name',
  linestring
from ways 
  where tags ? 'waterway';

insert into water_polygons 
select 
  tags -> 'water',
  tags -> 'name',
  geom
from polygons 
where
  tags ? 'water';    

insert into water_polygons 
select 
  tags -> 'water',
  tags -> 'name',
  geom
from polygons 
where
  not tags ? 'water' and tags @> 'waterway=>riverbank';

insert into railroads
select
  tags->'railway',  
  linestring
from ways
where
  tags ? 'railway';

insert into amenity_polygons 
select
  tags -> 'amenity',
  tags -> 'name',
  tags -> 'building' in ('yes', '1', 'true'),
  false,
  geom
from polygons 
where
  tags ? 'amenity';

insert into poi
select 
  'amenity',
  tags -> 'amenity',
  tags -> 'name',
  concat_ws('; ', tags -> 'phone', tags -> 'contact:phone'),
  concat_ws('; ', tags -> 'website', tags -> 'contact:website'),
  tags->'opening_hours',
  false,  
  geom
from nodes
where
  tags ? 'amenity';

update amenity_polygons ap
set has_point_nsd = true
from poi p
where 
  St_Contains(ap.geom, p.geom) and p.primary_type = 'amenity' and p.secondary_type = ap.type;

insert into poi
select 
  'shop',
  tags -> 'shop',
  tags -> 'name',
  concat_ws('; ', tags -> 'phone', tags -> 'contact:phone'),
  concat_ws('; ', tags -> 'website', tags -> 'contact:website'),
  tags->'opening_hours',
  false,  
  geom
from nodes 
where
  tags ? 'shop';

insert into poi
select 
  'tourism',
  tags -> 'tourism',
  tags -> 'name',
  concat_ws('; ', tags -> 'phone', tags -> 'contact:phone'),
  concat_ws('; ', tags -> 'website', tags -> 'contact:website'),
  tags->'opening_hours',
  false,  
  geom
from nodes 
where
  tags ? 'tourism';

insert into poi
select 
  'pt',
  tags -> 'bus_stop',
  tags -> 'name',
  null,
  null,
  tags->'opening_hours',
  false,  
  geom
from nodes 
where
  tags @> 'highway=>bus_stop';

insert into coastline
select linestring from ways where tags @> 'natural=>coastline';

insert into barier_line 
select
  tags -> 'barrier',
  tags -> 'name',
  linestring
from ways
where tags ? 'barrier';

insert into barier_point 
select
  tags -> 'barrier',
  tags -> 'name',
  tags -> 'access',
  geom
from nodes
where tags ? 'barrier';

insert into landuse 
select
  tags -> 'landuse',
  tags -> 'name',
  geom
from polygons
where tags ? 'landuse';

insert into leisure 
select
  tags -> 'leisure',
  tags -> 'name',
  geom
from polygons
where tags ? 'leisure';
