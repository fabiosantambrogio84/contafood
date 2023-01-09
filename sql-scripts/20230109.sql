-- eseguire più volte fino ad ottenere 0 rows
UPDATE ddt
    JOIN (
    select
    -- progressivo,
    min(id) as id
    from ddt
    where progressivo in (
    select d.progressivo from ddt d where d.progressivo > 0 group by d.progressivo having count(d.id) > 1 order by d.progressivo
    )
    group by
    ddt.progressivo
    ) b ON ddt.id = b.id
    SET progressivo = (progressivo * -1)
;